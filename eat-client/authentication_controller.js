const axios = require('axios').default;

const URL = 'http://localhost:8081/users';

function register(user) {       
    axios.post(URL + "/register", user, {
        headers: {
            'Content-Type': 'application/json'
        }
    }).catch((error) => {
        console.log(error);
    });
}

module.exports = { register }