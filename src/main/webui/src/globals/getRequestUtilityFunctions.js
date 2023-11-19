import axios from "axios";
import * as UtilityFunctions from "@/globals/utilityFunctions";
import * as SessionStorageFunctions from "@/globals/sessionStorageUtilitiyFunctions";

export async function getAllExercisesFromDB() {
    try {
        const response = await axios.get('exercise/get/specific', {
            headers: {
                accept: 'application/json',
                Authorization: `Bearer ${UtilityFunctions.getJwtTokenFromSessionStorage()}`
            }
        });
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
                Authorization: `Bearer ${UtilityFunctions.getJwtTokenFromSessionStorage()}`
            }
        });
        SessionStorageFunctions.saveAllGoalkeepersInSessionStorage(response.data);
        return response.data;
    } catch (error) {
        console.error(error);
        return [];
    }
}

export async function getAllTrainingSessions() {
    try {
        const response = await axios.get('training_session/get/specific', {
            headers: {
                accept: 'application/json',
                Authorization: `Bearer ${UtilityFunctions.getJwtTokenFromSessionStorage()}`
            }
        });
        SessionStorageFunctions.saveAllTrainingSessionInSessionStorage(response.data);
        return response.data;
    } catch (error) {
        console.error(error);
        return [];
    }
}

export async function getTrainingSessionAsPdf(id) {
    try {
        const response = await axios.get('training_session/download/'+id, {
            headers: {
                accept: 'application/json',
                Authorization: `Bearer ${UtilityFunctions.getJwtTokenFromSessionStorage()}`
            },
            responseType: 'blob'
        });
        return response.data;
    } catch (error) {
        console.error(error);
        return [];
    }
}

export async function getTrainingSessionAsMail(id, mail) {
    try {
        const response = await axios.get('training_session/mail/'+id+'/'+mail, {
            headers: {
                accept: 'application/json',
                Authorization: `Bearer ${UtilityFunctions.getJwtTokenFromSessionStorage()}`
            },
        });
        return response.data;
    } catch (error) {
        console.error(error);
        return [];
    }
}

