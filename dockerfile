# Usamos una imagen ligera de Python 3.12
FROM debian:bookworm

# Evita que Python genere archivos .pyc y permite ver logs en tiempo real
ENV PYTHONDONTWRITEBYTECODE=1
ENV PYTHONUNBUFFERED=1

# Directorio de trabajo
WORKDIR /app

# Instalamos dependencias del sistema necesarias para nmap
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    nmap \
    python3 \
    python3-pip \
    python3-setuptools \
    python3-venv \
    && apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Copiamos archivos de configuración de dependencias
COPY pyproject.toml .

RUN python3 -m venv .venv
ENV PATH="/venv/bin:$PATH"

# Instalamos las dependencias usando pip
# Nota: Instalamos setuptools primero por el build-backend definido
RUN pip install --upgrade pip && \
    pip install "setuptools>=77.0.3" && \
    pip install .

# Copiamos el resto del código (asumiendo que tu código vive en /src)
COPY . .

# Exponemos el puerto de Django
EXPOSE 8000

# Comando para iniciar el servidor de desarrollo
CMD ["python", "src/manage.py", "runserver", "0.0.0.0:8000"]