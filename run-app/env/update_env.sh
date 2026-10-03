#!/usr/bin/env bash

if ! command -v aws &>/dev/null; then
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
	if [ ! -f "$arquivo" ]; then
	    echo "ERRO: O arquivo local '$arquivo' não foi encontrado nesta pasta."
	    echo "Certifique-se de rodar o script no mesmo diretório do arquivo .env"
	    exit 1
	fi

	echo "🔎 Verificando a versão atual na AWS..."
	temp_ssm=$(mktemp)

	resultado_json=$(aws ssm get-parameter --name "${PREFIXO_SSM}/${arquivo}" --with-decryption --output json)

	if [ $? -eq 0 ]; then
		echo "$resultado_json" | jq -r '.Parameter.Value' > "$temp_ssm"
	elif echo "${resultado}" | grep -q "ParameterNotFound"; then
		echo "Parâmetro ainda não existe na AWS. Será criado pela primeira vez."
	else
        	echo "ERRO: Falha ao comunicar com a AWS"
		echo "Atualização será abortada"
		exit 1
	fi

	text_diff=$(git diff --color --no-index "$temp_ssm" "$arquivo")
	resultado_diff=$?
	if [ $resultado_diff -eq 0 ]; then
		echo "Os arquivos são idênticos. Não há necessidade de fazer upload."
	else
		echo "ATENÇÃO: Existem diferenças entre a nuvem e o seu arquivo local."
		echo "================= DIFERENÇAS ====================="
		echo "$text_diff"
		echo "=================================================="	    
	 	read -p "Você tem certeza que deseja sobrescrever a nuvem com o seu arquivo? (s/N): " confirmacao

	 	if [[ "$confirmacao" =~ ^[sS]$ ]]; then
			echo "Enviando atualização..."
			
			aws ssm put-parameter \
		    	--name "${PREFIXO_SSM}/${arquivo}" \
		    	--value file://"$arquivo" \
		    	--type "SecureString" \
		    	--region "${ENCURTA_URL_REGION}" \
		    	--overwrite

			echo "Upload concluído com sucesso!"
	    else
			echo "Upload cancelado."
	    fi
fi

rm -f "$temp_ssm"
done
