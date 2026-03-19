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
            # Convertir la cadena en lista de enteros
            return [int(p.strip()) for p in data.split(",") if p.strip()]
        except ValueError:
            raise forms.ValidationError(
                "Los puertos deben ser números enteros separados por comas.")
