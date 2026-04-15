from django.db import models


class ScanResult(models.Model):
    dominio = models.CharField(max_length=255)
    fecha = models.DateTimeField(auto_now_add=True)
    # Guardaremos el resultado como texto para evitar complicaciones de drivers
    data_json = models.TextField(blank=True, null=True)

    def __str__(self):
        return f"Scan {self.dominio} - {self.fecha}"
