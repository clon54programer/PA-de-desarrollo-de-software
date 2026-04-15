from django.urls import path

from . import views

urlpatterns = [
    path("", views.index, name="index"),
    path("scan", views.scanner, name="scan"),
    path("result", views.view_result, name="result"),
]
