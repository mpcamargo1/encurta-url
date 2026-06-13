PASTA_REFERENCIAL_RUN_APP=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)

cd ${PASTA_REFERENCIAL_RUN_APP} && cd ../frontend/encurta-url

ng build --configuration development

if [ $? -ne 0 ]; then
    echo "✘ Ops, o build falhou. Abortando..."
    exit 1
fi

cd ${PASTA_REFERENCIAL_RUN_APP} && rm -rfv frontend && mkdir frontend

cd ../frontend/encurta-url && cp -v -rp dist/encurta-url/browser/* ../../run-app/frontend && cd ${PASTA_REFERENCIAL_RUN_APP}

docker compose up --build
