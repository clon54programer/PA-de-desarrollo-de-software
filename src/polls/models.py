from django.db import models


class HostScan(models.Model):
    ip_address = models.GenericIPAddressField()
    hostname = models.CharField(max_length=255, blank=True, null=True)
    hostname_type = models.CharField(max_length=50, blank=True, null=True)
    scan_date = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"{self.ip_address} ({self.hostname})"


class ServiceResult(models.Model):
    host_scan = models.ForeignKey(
        HostScan, on_delete=models.CASCADE, related_name="services"
    )
    protocol = models.CharField(max_length=10)  # tcp o udp
    port = models.IntegerField()
    name = models.CharField(max_length=100, blank=True, null=True)
    state = models.CharField(max_length=50, blank=True, null=True)
    product = models.CharField(max_length=255, blank=True, null=True)
    version = models.CharField(max_length=100, blank=True, null=True)
    extrainfo = models.CharField(max_length=255, blank=True, null=True)
    reason = models.CharField(max_length=100, blank=True, null=True)
    conf = models.CharField(max_length=50, blank=True, null=True)
    cpe = models.CharField(max_length=255, blank=True, null=True)

    def __str__(self):
        return f"{self.protocol}/{self.port} - {self.state}"


class ScanResult(models.Model):
    dominio = models.CharField(max_length=255)
    fecha = models.DateTimeField(auto_now_add=True)
    servicio = models.ForeignKey(
        ServiceResult, on_delete=models.CASCADE, related_name="escaneos"
    )

    def __str__(self):
        return f"Scan {self.dominio} - {self.fecha}"
