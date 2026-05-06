from django.urls import path

from . import views

urlpatterns = [
    path("", views.index, name="index"),
    path("scan", views.scanner, name="scan"),
    path("result", views.view_result, name="result"),
    path("service_result", views.get_services, name="service_result"),
    path("pdf_generate", views.pdf_generate, name="pdf_generate"),
]
