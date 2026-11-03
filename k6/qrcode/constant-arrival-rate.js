import http from 'k6/http';
import { sleep, check } from 'k6';

export const options = {
  scenarios: {
    estresse_funcional: {
      executor: 'constant-arrival-rate',
      rate: 500,
      timeUnit: '1s',
      duration: '180s',
      preAllocatedVUs: 100,
      maxVUs: 1000,
    },
  },
  summaryTrendStats: ['avg', 'min', 'med', 'p(95)', 'p(99)'],
};

export default function() {

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
