const axios = require('axios').default;

const URL = 'http://localhost:8081/users/auth';
const requestSettings =  {
    headers: {
        'Content-Type': 'application/json'
    }
};

/**
 * Register a user with a username and password.
 * Also get a token after succesful registration
 * 
 * @param user - the user to register
 * @returns JWT if registered successfully
 */
async function register(user) {       
    await axios.post(URL + "/register", user, requestSettings)
    .then((result) => {
        return result.data.token;
    }).catch((error) => {
        console.log(error);
        return null;
    });
}

/**
 * Authenticate a user with a username and password.
 * Returns a JWT if authentication was succesful.
 * 
 * @param user - contains username and password of user to authenticate
 * @returns JWT if authenticated successffuly
 */
async function authenticate(user) {
    try {
        const response = await axios.post('http://localhost:8081/users/auth/authenticate', user, requestSettings);
        return response.data.token;
    } catch (error) {
        console.error(error);
    }
}

module.exports = { register, authenticate }