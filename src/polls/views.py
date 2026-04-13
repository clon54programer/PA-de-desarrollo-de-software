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

    if request.method != "GET" and request.method != "POST":
        return render(request, "polls/404.html")

    if request.method == "GET":
        return render(
            request,
            "polls/scan.html",
            {"title": "scanner", "form": PortsAndDomionsForm()},
        )

    form = PortsAndDomionsForm(request.POST)

    if not form.is_valid():
        message = "El formulario no es valido"
        return render(
            request,
            "polls/scan.html",
            {"title": "scanner", "form": PortsAndDomionsForm(), "message": message},
        )

    print(form)
    form.clean_puertos()

    print("flags: ", form.get_flags())
    nm = None
    try:
        nm = nmap.PortScanner()
    except nmap.nmap.PortScannerError:
        return render(
            request,
            "polls/error.html",
            {"title": "error", "message": "NMAP no esta en el path"},
        )
    if form.flags is None:
        nm.scan(form.get_dominio(), form.get_ports())
    else:
        nm.scan(form.get_dominio(), form.get_ports(), arguments=form.get_flags())

    return render(request, "polls/scan_result.html", {"title": form.get_dominio()})
