cd ../frontend/encurta-url

ng build --configuration development

rm -rfv ../../run-app/frontend

mkdir ../../run-app/frontend

cp -v -rp dist/encurta-url/browser/* ../../run-app/frontend

cd ../../run-app

docker compose up --build
