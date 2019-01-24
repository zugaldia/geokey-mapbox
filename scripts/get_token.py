import requests

result = requests.post('https://planetxy.com/api-token/', data={'username':'', 'password': ''})
print(result.status_code)
print(result.text)
