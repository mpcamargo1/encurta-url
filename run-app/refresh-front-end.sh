PASTA_REFERENCIAL_RUN_APP=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)

cd ${PASTA_REFERENCIAL_RUN_APP} && cd ../frontend/encurta-url

ng build --configuration development

if [ $? -ne 0 ]; then
     echo "✘ Ops, o build falhou. Abortando..."
     exit 1
fi

docker exec nginx-proxy bash -c "rm -r /usr/share/nginx/html/*"
docker cp dist/encurta-url/browser/. nginx-proxy:/usr/share/nginx/html/ && docker exec -it nginx-proxy nginx -s reload
