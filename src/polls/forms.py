from django import forms


class PortsAndDomionsForm(forms.Form):
    dominio = forms.CharField(
        label="Dominio",
        max_length=255,
        required=True,
        widget=forms.TextInput(
            attrs={'placeholder': 'ejemplo.com  o 192.168.1.1'})
    )

    puertos = forms.CharField(
        label="Puertos",
        required=False,
        widget=forms.Textarea(attrs={'placeholder': '80, 443, 22'}),
        help_text="Lista de puertos separados por comas. Puede dejarse vacío."
    )

    def clean_puertos(self):
        data = self.cleaned_data.get("puertos")
        if not data:
            return []
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
                "Los puertos deben ser números enteros separados por comas.")

    def __str__(self):
        dominio = self.cleaned_data.get("dominio", None)
        puertos = self.cleaned_data.get("puertos", None)
        return f"dominio: {dominio}\npuertos: {puertos}"

    def get_dominio(self):
        return self.cleaned_data.get("dominio", None)

    def get_ports(self):
        return self.cleaned_data.get("puertos", None)
