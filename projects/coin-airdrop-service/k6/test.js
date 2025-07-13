import http from 'k6/http';
import { check, sleep } from 'k6';

// export let options = {
//     vus: 100,
//     duration: '1s',
// };

export let options = {
    vus: 10,
    iterations: 100,
};

export default function () {
    const userId = Math.floor(Math.random() * 1000000);
    const headers = { 'X-USER-ID': userId };
    const res = http.post('http://localhost:8080/airdrop/claim', null, { headers });

    check(res, {
        'status is 200 or 409': (r) => r.status === 200 || r.status === 409,
    });

    sleep(0.1);
}