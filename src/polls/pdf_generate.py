from django.template.loader import render_to_string
from weasyprint import HTML
from django.http import HttpResponse
import os
from django.conf import settings
import base64


def exportar_pdf(request, data):
    """
    ## Data
    es title y service
    """
    # 1. Renderiza el HTML con tus datos
    logo_path = os.path.join(settings.BASE_DIR, "static", "CyberNetLogo.jpeg")
    try:
        with open(logo_path, "rb") as image_file:
            # Convertimos la imagen a un string que el HTML entiende directamente
            encoded_string = base64.b64encode(image_file.read()).decode()
            data["logo_base64"] = f"data:image/jpeg;base64,{encoded_string}"
    except FileNotFoundError:
        data["logo_base64"] = ""  # Si no existe, que no rompa el PDF
        print(f"[ERROR] No se encontró el logo en: {logo_path}")

    html_string = render_to_string("polls/pdf_template.html", data)

    # 2. Genera el PDF
    html = HTML(string=html_string, base_url=request.build_absolute_uri())
    result = html.write_pdf()

    return result
