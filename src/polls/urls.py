from django.urls import path

from . import views

from django.contrib.auth import views as auth_views

urlpatterns = [
    path("", views.index, name="index"),
    path("scan", views.scanner, name="scan"),
    path("result", views.view_result, name="result"),
    path("service_result", views.get_services, name="service_result"),
    path("pdf_generate", views.pdf_generate, name="pdf_generate"),
    path(
        "login",
        auth_views.LoginView.as_view(template_name="polls/login.html"),
        name="login",
    ),
    path("logout/", auth_views.LogoutView.as_view(), name="logout"),
]
