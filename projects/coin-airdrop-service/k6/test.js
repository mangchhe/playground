import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter } from 'k6/metrics';

let successCount = new Counter('success_requests');
let conflictCount = new Counter('conflict_requests');
let soldoutCount = new Counter('soldout_requests');

// export let options = {
//     vus: 100,
//     duration: '1s',
// };

export let options = {
    vus: 1000,
    iterations: 100000,
};

export default function () {
    const userId = Math.floor(Math.random() * 1000000);
    const headers = { 'X-USER-ID': userId };
    const res = http.post('http://localhost:8080/airdrop/claim', null, { headers });

    check(res, {
        'status is 200 or 409': (r) => r.status === 200 || r.status === 409,
    });

    if (res.status === 200) {
        successCount.add(1);
    } else if (res.status === 409) {
        conflictCount.add(1);
    } else if (res.status === 410) {
        soldoutCount.add(1);
    }

    sleep(0.1);
}