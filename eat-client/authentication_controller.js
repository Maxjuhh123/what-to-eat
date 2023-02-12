const axios = require('axios').default;

const URL = 'http://localhost:8081/users/auth';

function register(user) {       
    const res = axios.post(URL + "/register", user, {
        headers: {
            'Content-Type': 'application/json'
        }
    }).then((result) => {
        return result.data.token;
    }).catch((error) => {
        console.log(error);
        return null;
    });
}

module.exports = { register }