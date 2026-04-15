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

    def nmap_object_to_model(scanner):
        # scanner es de tipo PortScanner
        for host in scanner.all_hosts():
            hostnames = scanner[host].get("hostnames", [])
            h_name = hostnames[0]["name"] if hostnames else ""
            h_type = hostnames[0]["type"] if hostnames else ""

            # 2. Crear o actualizar el registro del Host
            host_obj, created = HostScan.objects.get_or_create(
                ip_address=host, defaults={"hostname": h_name, "hostname_type": h_type}
            )

        for proto in self[host].all_protocols():
            if proto not in ["tcp", "udp"]:
                continue

            lport = list(self[host][proto].keys())
            lport.sort()

        for port in lport:
            port_info = self[host][proto][port]

            # 3. Guardar el servicio asociado al host
            ServiceResult.objects.update_or_create(
                host_scan=host_obj,
                protocol=proto,
                port=port,
                defaults={
                    "name": port_info.get("name"),
                    "state": port_info.get("state"),
                    "product": port_info.get("product"),
                    "version": port_info.get("version"),
                    "extrainfo": port_info.get("extrainfo"),
                    "reason": port_info.get("reason"),
                    "conf": port_info.get("conf"),
                    "cpe": port_info.get("cpe"),
                },
            )
