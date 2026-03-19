from django.http import HttpResponse
from django.shortcuts import render
from django.contrib.auth.decorators import login_required
import nmap
from .forms import PortsAndDomionsForm
# Create your views here.


def index(request):
    if not request.user.is_authenticated:
        return render(request, "polls/404.html")
    return render(request, "polls/index.html", {"message": f"Hello world, {request.user.username}"})


@login_required
def scanner(request):

    if request.method != "GET" or request.method != "POST":
        return render(request, "polls/404.html")

    if request.method == "GET":
        return render(request, "polls/scan.html", {
            "title": "scanner",
            "form": PortsAndDomionsForm()
        })
