from django.template.loader import render_to_string
from weasyprint import HTML
from django.http import HttpResponse


def exportar_pdf(request, data):
    """
    ## Data
    es title y service
    """
    # 1. Renderiza el HTML con tus datos
    context = data
    html_string = render_to_string("pdf_template.html", context)

    # 2. Genera el PDF
    html = HTML(string=html_string, base_url=request.build_absolute_uri())
    result = html.write_pdf()
