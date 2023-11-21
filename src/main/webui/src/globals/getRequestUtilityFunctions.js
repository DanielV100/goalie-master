/**
 * There are a lot of http-get-requests in the application.
 * To have a better overview, every get-requests is here.
 * @author daniel
 */
import axios from "axios";
import * as SessionStorageFunctions from "@/globals/sessionStorageUtilitiyFunctions";

/**
 * Gets all exercises from current user from the db.
 * @returns {Promise<any|*[]>}
 */
export async function getAllExercisesFromDB() {
    try {
        const response = await axios.get('exercise/get/specific', {
            headers: {
                accept: 'application/json',
                Authorization: `Bearer ${SessionStorageFunctions.getJwtTokenFromSessionStorage()}`
            }
        });
        console.log(response.data);
        SessionStorageFunctions.saveAllExercisesInSessionStorage(response.data);
        return response.data;
    } catch (error) {
        console.error(error);
        return [];
    }
}

/**
 * Method makes http-request and gets goalkeepers from db.
 * @returns {Promise<any|*[]>}
 */
export async function getGoalkeepersFromDB() {
    try {
        const response = await axios.get('goalkeeper/get/specific', {
            headers: {
                accept: 'application/json',
                Authorization: `Bearer ${SessionStorageFunctions.getJwtTokenFromSessionStorage()}`
            }
        });
        SessionStorageFunctions.saveAllGoalkeepersInSessionStorage(response.data);
        console.log(response.data);
        return response.data;
    } catch (error) {
        console.error(error);
        return [];
    }
}

/**
 * Gets all training sessions from current user from the db.
 * @returns {Promise<any|*[]>}
 */
export async function getAllTrainingSessions() {
    try {
        const response = await axios.get('training_session/get/specific', {
            headers: {
                accept: 'application/json',
                Authorization: `Bearer ${SessionStorageFunctions.getJwtTokenFromSessionStorage()}`
            }
        });
        SessionStorageFunctions.saveAllTrainingSessionInSessionStorage(response.data);
        return response.data;
    } catch (error) {
        console.error(error);
        return [];
    }
}

/**
 * Gets one training session as a pdf file.
 * @param id
 * @returns {Promise<any|*[]>}
 */
export async function getTrainingSessionAsPdf(id) {
    try {
        const response = await axios.get('training_session/download/'+id, {
            headers: {
                accept: 'application/json',
                Authorization: `Bearer ${SessionStorageFunctions.getJwtTokenFromSessionStorage()}`
            },
            responseType: 'blob'
        });
        const contentDisposition = response.headers['content-disposition'];
        if (contentDisposition) {
            const filenameMatch = contentDisposition.match(/filename="([^"]+)"/);
            const filename = filenameMatch ? filenameMatch[1] : null;
            response.data["name"] = filename;
        }
        return response.data;
    } catch (error) {
        console.error(error);
        return [];
    }
}

/**
 * Triggers backend to send a mail with a training session attached to it.
 * @param id
 * @param mail
 * @returns {Promise<*>}
 */
export async function getTrainingSessionAsMail(id, mail) {
    try {
        const response = await axios.get('training_session/mail/'+id+'/'+mail, {
            headers: {
                accept: 'application/json',
                Authorization: `Bearer ${SessionStorageFunctions.getJwtTokenFromSessionStorage()}`
            },
        });
        return 'Mail wurde versand';
    } catch (error) {
        return 'Es ist ein Fehler aufgetreten: ' + error;
    }
}

