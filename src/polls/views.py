from django.http import HttpResponse
from django.shortcuts import render
# Create your views here.


def index(request):
    if not request.user.is_authenticated:
        return render(request, "polls/404.html")
    return render(request, "polls/index.html", {"message": f"Hello world, {request.user.username}"})
