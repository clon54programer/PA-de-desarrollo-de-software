@echo off


python manage.py migrate
python manage.py makemigrations polls