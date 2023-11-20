/**
 * Central part of user login and management is the jwt token.
 * All jwt token related functions can be found here.
 * @author daniel
 */

/**** IMPORTS ****/
import * as SessionStorageFunctions from '@/globals/sessionStorageUtilitiyFunctions.js';

/**** EXPORT FUNCTIONS ****/
/**
 * Only authenticated users should use the application, therefor this method
 * checks if jwt token is expired or not.
 * @returns {boolean}
 */
export function checkIfJwtTokenIsValid() {
    const jwtToken = SessionStorageFunctions.getJwtTokenFromSessionStorage();
    if(jwtToken) {
        const payload = getPayloadFromJwtToken(jwtToken);
        const currentTime = Math.floor(Date.now() / 1000);
        return payload.exp > currentTime;
    } else {
        return false;
    }
}

/**** PRIVATE FUNCTIONS ****/
/**
 * Gets the payload from the passed jwt token and parses it into json.
 * @param jwtToken
 * @returns payload of jwt token
 */
function getPayloadFromJwtToken(jwtToken) {
    const base64Payload = jwtToken.split('.')[1].replace(/-/g, '+').replace(/_/g, '/');
    return JSON.parse(window.atob(base64Payload));
}

