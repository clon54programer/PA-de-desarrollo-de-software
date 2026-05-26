from django.http import HttpResponse
from django.shortcuts import render, redirect
from django.contrib.auth.decorators import login_required
import nmap
from .forms import PortsAndDomionsForm
import threading
from .models import ScanResult, ServiceResult, HostScan
import json
from django.utils.text import slugify

from .pdf_generate import exportar_pdf

# Create your views here.


def index(request):
    if not request.user.is_authenticated:
        return redirect("/login")
    return render(
        request,
        "polls/index.html",
        {"message": f"Hello world, {request.user.username}"},
    )


def scan_task(dominio, puertos, flags, scan_name):
    print(f"--- Iniciando escaneo de fondo para {dominio} ---")
    nm = nmap.PortScanner()
    try:
        puertos_str = ""
        if isinstance(puertos, list):
            # Convertimos cada elemento a str por si vienen como enteros [80, 443]
            puertos_str = ",".join(str(p) for p in puertos)
        else:
            puertos_str = str(puertos)

        if isinstance(flags, list):
            flags_str = " ".join(str(f) for f in flags)
        elif flags is None:
            flags_str = ""
        else:
            flags_str = str(flags)

        # Imprime esto en tu consola para verificar qué le estás mandando exactamente a Nmap
        print(f"[DEBUG] Puertos: {puertos_str} | Flags: '{flags_str}'")
        print(f"puertos_str: {puertos_str}")

        nm.scan(hosts=dominio, ports=puertos_str, arguments=flags_str)
        # Aquí es donde guardarías en la base de datos en un proyecto real
        print(f"--- Escaneo finalizado para {dominio} {scan_name} ---")
        print(nm[dominio].get("tcp", {}))

        ScanResult.nmap_object_to_model(nm, dominio, scan_name)
        print("[INFO] Se guardo un resultado")

    except Exception as e:
        print(f"Error en el hilo de nmap: {e}")


@login_required
def scanner(request):
    if request.method == "GET":
        return render(
            request,
            "polls/scan.html",
            {"title": "scanner", "form": PortsAndDomionsForm()},
        )

    if request.method == "POST":
        form = PortsAndDomionsForm(request.POST)
        if not form.is_valid():
            return render(
                request,
                "polls/scan.html",
                {
                    "title": "scanner",
                    "form": form,
                    "message": "El formulario no es válido",
                },
            )

        dominio = form.get_dominio()
        puertos = form.get_ports()
        flags = form.get_flags() or ""
        scan_name = form.get_scan_name()

        hilo = threading.Thread(
            target=scan_task, args=(dominio, puertos, flags, scan_name)
        )
        # Lo iniciamos (no bloquea la vista)
        hilo.start()
        return render(request, "polls/scan_peding.html", {"dominio": dominio})

    return render(request, "polls/404.html")


@login_required
def view_result(request):
    if request.method != "GET":
        return render(request, "polls/404.html")
    scan_result = ScanResult.objects.all()
    is_null = True
    if scan_result != None:
        is_null = False

    return render(
        request,
        "polls/scan_result.html",
        {"is_null": is_null, "results": scan_result, "title": "resultados"},
    )


@login_required
def get_services(request):
    if request.method != "GET":
        return render(request, "polls/404.html")
    service_id = request.GET.get("service_id", None)
    print("[INFO] Servicio id: " + service_id)

    if service_id == None:
        return render(
            request,
            "polls/scan_service_result.html",
            {"title": "error", "id": None},
        )

    service = ServiceResult.objects.get(id=service_id)

    # Aquí podrías usar esa info para filtrar tu base de datos
    return render(
        request,
        "polls/scan_service_result.html",
        {"title": service.name, "service": service, "id": service_id},
    )


@login_required
def pdf_generate(request):
    if request.method != "GET":
        return render(request, "polls/404.html")
    service_id = request.GET.get("service_id", None)
    print("[INFO] Servicio id: " + service_id)
    service = ServiceResult.objects.get(id=service_id)

    result = exportar_pdf(request, {"title": service.name, "service": service})

    response = HttpResponse(result, content_type="application/pdf")
    filename = slugify(service.name) or "documento"
    response["Content-Disposition"] = f'inline; filename="{filename}.pdf"'
    return response
