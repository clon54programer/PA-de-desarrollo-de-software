from django.http import HttpResponse
from django.shortcuts import render
from django.contrib.auth.decorators import login_required
import nmap
from .forms import PortsAndDomionsForm

# Create your views here.


def index(request):
    if not request.user.is_authenticated:
        return render(request, "polls/404.html")
    return render(
        request,
        "polls/index.html",
        {"message": f"Hello world, {request.user.username}"},
    )


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

        try:
            nm = nmap.PortScanner()
            dominio = form.cleaned_data.get("dominio")
            puertos = form.cleaned_data.get("puertos")
            flags = form.get_flags() or ""

            if dominio in nm.all_hosts():
                resultados = nm[dominio]
            else:
                # Si nmap terminó pero no encontró el host
                return render(
                    request,
                    "polls/error.html",
                    {"message": "No se encontraron resultados para ese host."},
                )

            return render(
                request,
                "polls/scan_result.html",
                {"title": dominio, "resultados": resultados},
            )

        except nmap.PortScannerError as e:
            return render(request, "polls/error.html", {"message": f"Nmap error: {e}"})
        except Exception as e:
            # Aquí capturarás el Broken Pipe y verás el mensaje real
            return render(
                request, "polls/error.html", {"message": f"Error inesperado: {str(e)}"}
            )

    return render(request, "polls/404.html")
