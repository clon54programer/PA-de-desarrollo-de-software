from django.http import HttpResponse
from django.shortcuts import render
from django.contrib.auth.decorators import login_required
import nmap
from .forms import PortsAndDomionsForm
import threading
from .models import ScanResult
import json

# Create your views here.


def index(request):
    if not request.user.is_authenticated:
        return render(request, "polls/404.html")
    return render(
        request,
        "polls/index.html",
        {"message": f"Hello world, {request.user.username}"},
    )


def scan_task(dominio, puertos, flags):
    print(f"--- Iniciando escaneo de fondo para {dominio} ---")
    nm = nmap.PortScanner()
    try:
        nm.scan(hosts=dominio, ports=puertos, arguments=flags)
        # Aquí es donde guardarías en la base de datos en un proyecto real
        print(f"--- Escaneo finalizado para {dominio} ---")
        print(nm[dominio].get("tcp", {}))
        if dominio in nm.all_hosts():
            save_scan_result = ScanResult()
            save_scan_result.dominio = dominio
            save_scan_result.data_json = json.dump(nm[dominio])
            save_scan_result.save()
            print("[INFO] Se guardo un resultado\n" + save_scan_result)

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

        hilo = threading.Thread(target=scan_task, args=(dominio, puertos, flags))
        # Lo iniciamos (no bloquea la vista)
        hilo.start()
        return render(request, "polls/scan_peding.html", {"dominio": dominio})

    return render(request, "polls/404.html")
