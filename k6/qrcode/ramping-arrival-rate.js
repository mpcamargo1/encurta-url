import http from 'k6/http';
import {check} from 'k6';

export const options = {
  scenarios: {
    // Fase 1: Aquecimento
    aquecimento: {
      executor: 'constant-arrival-rate',
      rate: 50,
      timeUnit: '1s',
      duration: '1m',
      preAllocatedVUs: 20,
      maxVUs: 100,
      startTime: '0s',
    },

    // Fase 2: Subida / Estresse inicial
    estresse: {
      executor: 'constant-arrival-rate',
      rate: 300,
      timeUnit: '1s',
      duration: '2m',
      preAllocatedVUs: 100,
      maxVUs: 500,
      startTime: '1m',
    },

    // Fase 3: Sustentação no talo
    sustentacao: {
      executor: 'constant-arrival-rate',
      rate: 1200,
      timeUnit: '1s',
      duration: '5m',
      preAllocatedVUs: 100,
      maxVUs: 500,
      startTime: '3m',
    },
  },
};

export default function () {
  const payload = JSON.stringify({
        url: "https://youtube.com.br"
  });

  const params = {
        headers: {
                'Content-Type': 'application/json'
        }
  }

  let res = http.post('http://localhost/qrcode', payload, params);

  check(res, {
        'status é 200': (r) => r.status === 200,
   });
}
