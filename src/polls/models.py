from django.db import models

class Asset(models.Model):
    # SQLite no tiene INET, usamos GenericIPAddressField que se guarda como string
    ip_address = models.GenericIPAddressField(protocol='both', unpack_ipv4=False)
    hostname = models.CharField(max_length=255, blank=True, null=True)
    mac_address = models.CharField(max_length=17, blank=True, null=True)
    os_type = models.CharField(max_length=100, blank=True, null=True)
    os_version = models.CharField(max_length=100, blank=True, null=True)
    os_cpe = models.CharField(max_length=255, blank=True, null=True)
    environment = models.CharField(max_length=50, blank=True, null=True)
    criticality = models.CharField(max_length=20, blank=True, null=True)
    owner = models.CharField(max_length=100, blank=True, null=True)
    location = models.CharField(max_length=100, blank=True, null=True)
    first_seen = models.DateTimeField(auto_now_add=True)
    last_seen = models.DateTimeField(blank=True, null=True)
    is_active = models.BooleanField(default=True)
    notes = models.TextField(blank=True, null=True)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    def __str__(self):
        return f"{self.hostname or 'Unknown'} ({self.ip_address})"

class Service(models.Model):
    asset = models.ForeignKey(Asset, on_delete=models.CASCADE, related_name='services')
    port = models.IntegerField()
    protocol = models.CharField(max_length=10)
    service_name = models.CharField(max_length=100)
    service_version = models.CharField(max_length=200, blank=True, null=True)
    service_product = models.CharField(max_length=200, blank=True, null=True)
    service_extrainfo = models.TextField(blank=True, null=True)
    service_cpe = models.CharField(max_length=255, blank=True, null=True)
    state = models.CharField(max_length=20, blank=True, null=True)
    banner = models.TextField(blank=True, null=True)
    first_detected = models.DateTimeField(auto_now_add=True)
    last_detected = models.DateTimeField(blank=True, null=True)
    is_active = models.BooleanField(default=True)

    class Meta:
        unique_together = ('asset', 'port', 'protocol')

    def __str__(self):
        return f"{self.port}/{self.protocol} - {self.service_name}"

class CVECatalog(models.Model):
    id = models.CharField(max_length=20, primary_key=True)
    description = models.TextField(blank=True, null=True)
    published_date = models.DateField(blank=True, null=True)
    last_modified_date = models.DateField(blank=True, null=True)
    cvss2_score = models.DecimalField(max_digits=3, decimal_places=1, blank=True, null=True)
    cvss2_vector = models.CharField(max_length=100, blank=True, null=True)
    cvss3_score = models.DecimalField(max_digits=3, decimal_places=1, blank=True, null=True)
    cvss3_vector = models.CharField(max_length=100, blank=True, null=True)
    severity = models.CharField(max_length=20, blank=True, null=True)
    exploit_available = models.BooleanField(default=False)
    exploitability_score = models.DecimalField(max_digits=3, decimal_places=1, blank=True, null=True)
    impact_score = models.DecimalField(max_digits=3, decimal_places=1, blank=True, null=True)
    attack_vector = models.CharField(max_length=50, blank=True, null=True)
    attack_complexity = models.CharField(max_length=20, blank=True, null=True)
    privileges_required = models.CharField(max_length=20, blank=True, null=True)
    user_interaction = models.CharField(max_length=20, blank=True, null=True)
    scope = models.CharField(max_length=20, blank=True, null=True)
    confidentiality_impact = models.CharField(max_length=20, blank=True, null=True)
    integrity_impact = models.CharField(max_length=20, blank=True, null=True)
    availability_impact = models.CharField(max_length=20, blank=True, null=True)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    def __str__(self):
        return self.id

class CVEAffectedProduct(models.Model):
    cve = models.ForeignKey(CVECatalog, on_delete=models.CASCADE, related_name='affected_products')
    cpe_string = models.CharField(max_length=500)
    vendor = models.CharField(max_length=200, blank=True, null=True)
    product = models.CharField(max_length=200, blank=True, null=True)
    version = models.CharField(max_length=100, blank=True, null=True)
    version_start_including = models.CharField(max_length=100, blank=True, null=True)
    version_end_including = models.CharField(max_length=100, blank=True, null=True)
    version_start_excluding = models.CharField(max_length=100, blank=True, null=True)
    version_end_excluding = models.CharField(max_length=100, blank=True, null=True)
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"{self.product} ({self.cve.id})"

class DetectedVulnerability(models.Model):
    asset = models.ForeignKey(Asset, on_delete=models.CASCADE)
    service = models.ForeignKey(Service, on_delete=models.CASCADE, blank=True, null=True)
    cve = models.ForeignKey(CVECatalog, on_delete=models.SET_NULL, null=True)
    detection_date = models.DateTimeField(auto_now_add=True)
    last_seen = models.DateTimeField(blank=True, null=True)
    status = models.CharField(max_length=20, default='active')
    confidence = models.CharField(max_length=20, blank=True, null=True)
    match_type = models.CharField(max_length=50, blank=True, null=True)
    notes = models.TextField(blank=True, null=True)
    assigned_to = models.CharField(max_length=100, blank=True, null=True)
    due_date = models.DateField(blank=True, null=True)
    remediation_steps = models.TextField(blank=True, null=True)
    verified_at = models.DateTimeField(blank=True, null=True)
    verified_by = models.CharField(max_length=100, blank=True, null=True)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    def __str__(self):
        return f"{self.cve_id} @ {self.asset.ip_address}"

class Scan(models.Model):
    scan_name = models.CharField(max_length=200, blank=True, null=True)
    scan_type = models.CharField(max_length=50, blank=True, null=True)
    # SQLite no soporta ArrayField. Guardamos los rangos como un string separado por comas.
    target_range = models.TextField(blank=True, null=True, help_text="List of CIDRs separated by commas")
    started_at = models.DateTimeField(auto_now_add=True)
    completed_at = models.DateTimeField(blank=True, null=True)
    status = models.CharField(max_length=20, blank=True, null=True)
    nmap_arguments = models.TextField(blank=True, null=True)
    total_hosts = models.IntegerField(blank=True, null=True)
    total_services = models.IntegerField(blank=True, null=True)
    vulnerabilities_found = models.IntegerField(blank=True, null=True)
    initiated_by = models.CharField(max_length=100, blank=True, null=True)
    error_log = models.TextField(blank=True, null=True)
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"{self.scan_name or 'Scan'} #{self.pk}"

class ScanResult(models.Model):
    scan = models.ForeignKey(Scan, on_delete=models.CASCADE, related_name='results')
    asset = models.ForeignKey(Asset, on_delete=models.SET_NULL, null=True)
    service = models.ForeignKey(Service, on_delete=models.SET_NULL, null=True)
    nmap_output = models.TextField(blank=True, null=True) 
    # Django emula JSONField en SQLite guardándolo como texto plano
    raw_data = models.JSONField(blank=True, null=True)
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"Result: {self.scan.scan_name} - {self.asset}"

class Exploit(models.Model):
    exploit_id = models.CharField(max_length=100, blank=True, null=True)
    cve = models.ForeignKey(CVECatalog, on_delete=models.CASCADE, related_name='exploits')
    source = models.CharField(max_length=50, blank=True, null=True)
    title = models.TextField(blank=True, null=True)
    type = models.CharField(max_length=50, blank=True, null=True)
    platform = models.CharField(max_length=50, blank=True, null=True)
    file_path = models.CharField(max_length=500, blank=True, null=True)
    verified = models.BooleanField(default=False)
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"Exploit {self.exploit_id} for {self.cve_id}"

class RemediationTicket(models.Model):
    vulnerability = models.ForeignKey(DetectedVulnerability, on_delete=models.CASCADE, related_name='tickets')
    ticket_number = models.CharField(max_length=50, unique=True)
    status = models.CharField(max_length=50, blank=True, null=True)
    priority = models.CharField(max_length=20, blank=True, null=True)
    assigned_to = models.CharField(max_length=100, blank=True, null=True)
    created_by = models.CharField(max_length=100, blank=True, null=True)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    resolved_at = models.DateTimeField(blank=True, null=True)
    resolution_notes = models.TextField(blank=True, null=True)
    due_date = models.DateField(blank=True, null=True)

    def __str__(self):
        return f"Ticket {self.ticket_number} [{self.status}]"