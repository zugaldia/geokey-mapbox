fixtures:
	rm -rf libgeokey/src/test/resources/*json
	curl -X GET \
		'https://planetxy.com/from-position/?lat=38.90962&lon=-77.04341' \
		-H 'Authorization: Token $(GEOKEY_TOKEN)' \
		-o libgeokey/src/test/resources/from_position.json
	curl -X GET \
		'https://planetxy.com/from-geokey/?geokey=TEPA%20FRID%20MOXA%20FULK' \
		-H 'Authorization: Token $(GEOKEY_TOKEN)' \
		-o libgeokey/src/test/resources/from_geokey.json
	curl -X GET \
		'https://planetxy.com/from-geokey/?geokey=XYZ' \
		-H 'Authorization: Token $(GEOKEY_TOKEN)' \
		-o libgeokey/src/test/resources/error.json
