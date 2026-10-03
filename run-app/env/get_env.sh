#!/usr/bin/env bash

if ! command -v aws &>/dev/null ; then
	echo "ERRO: AWS CLI deve ser instalado"
	exit 1
fi

VARIAVEIS_DE_AMBIENTE=(
	"PREFIXO_SSM"
	"ENCURTA_URL_REGION"
)

for variavel in "${VARIAVEIS_DE_AMBIENTE[@]}"; do
	if [ -z "${!variavel:-}" ]; then
	  echo "ERRO: Variável de ambiente não definida ${variavel}"
	  exit 1
	fi
done

ARQUIVOS_ENV=(
  "encurta-url.env"
  "qrcode.env"
)

for arquivo in "${ARQUIVOS_ENV[@]}"; do

	arquivo_env=$(aws ssm get-parameter \
		--name "${PREFIXO_SSM}/${arquivo}" \
		--with-decryption \
		--region "${ENCURTA_URL_REGION}" \
		--output json)

	if [ $? -eq 0 ]; then 
		echo "$arquivo_env" | jq -r  '.Parameter.Value' > "${arquivo}"
		echo "Arquivo .env ("${arquivo}") criado com sucesso"
	fi
done
