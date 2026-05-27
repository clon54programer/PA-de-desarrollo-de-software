from django import forms


class PortsAndDomionsForm(forms.Form):
    scan_name = forms.CharField(
        label="nombre del scan",
        max_length=120,
        required=True,
        widget=forms.TextInput(
            attrs={"class": "form-control", "placeholder": "scan name"}
        ),
    )
    dominio = forms.CharField(
        label="Dominio",
        max_length=255,
        required=True,
        widget=forms.TextInput(
            attrs={"class": "form-control", "placeholder": "ejemplo.com o 192.168.1.1"}
        ),
    )

    puertos = forms.CharField(
        label="Puertos",
        required=False,
        widget=forms.Textarea(
            attrs={
                "class": "form-control",
                "placeholder": "80, 443, 22",
                "rows": 2,  # Altura reducida
            }
        ),
        help_text="Lista de puertos separados por comas. Puede dejarse vacío.",
    )

    OPCIONES_NMAP = [
        ("", "Selecciona una opción..."),  # Opción por defecto vacía
        ("-sV", "Escaneo de servicios basico"),
        ("-sVe", "Escaneo de servicios avanzando"),
    ]

    flags = forms.ChoiceField(
        label="Parámetros",
        required=False,
        choices=OPCIONES_NMAP,
        # Usamos form-select que es la clase nativa de Bootstrap para los select
        widget=forms.Select(attrs={"class": "form-select"}),
        help_text="Son los parámetros que utiliza nmap para realizar sus escaneos",
    )

    def clean_flags(self):
        data = self.cleaned_data.get("flags")
        if not data:
            return ""
        try:
            str_flags = str(data)
            str_flags = str_flags.replace("[", "").replace("]", "")
            print("type: ", type(str_flags))
            print("data: ", str_flags)

            flags = []

            for f in str_flags.split(","):
                print("flag: ", f)
                flags.append(f)

            return flags
        except ValueError:
            raise forms.ValidationError(
                "Los parametros deben estar separados por comas."
            )

    """
    ## Advertencia
    No se debe usar esta funcion
    """

    def clean_puertos(self) -> None | list:
        data = self.cleaned_data.get("puertos")
        if not data:
            return None
        try:
            str_data = str(data)
            print(str_data)
            print(type(str_data))
            str_data = str_data.replace("[", "").replace("]", "")

            ports = []
            for number in str_data.split(","):
                print("number: ", number)
                ports.append(int(number))

            return ports
        except ValueError:
            raise forms.ValidationError(
                "Los puertos deben ser números enteros separados por comas."
            )

    def __str__(self):
        dominio = self.cleaned_data.get("dominio", None)
        puertos = self.cleaned_data.get("puertos", None)
        flags = self.cleaned_data.get("flags", None)
        return f"dominio: {dominio}\npuertos: {puertos}\nparametros: {flags}"

    def get_dominio(self):
        return self.cleaned_data.get("dominio", None)

    def get_ports(self):
        return self.cleaned_data.get("puertos", None)

    def get_flags(self):
        return self.cleaned_data.get("puertos")

    def get_scan_name(self):
        return self.cleaned_data.get("scan_name", None)
