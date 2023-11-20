/**
 * Utility class for doing http-delete requests.
 * @author daniel
 */

import axios from "axios";
import * as SessionStorageFunctions from "@/globals/sessionStorageUtilitiyFunctions";

/**
 * Generic method, which takes entity and id to send a delete-request.
 * @param entity
 * @param id
 * @returns {Promise<any|*[]>}
 */
export async function deleteEntityById(entity, id) {
    try {
        const response = await axios.delete(`${entity}/delete/${id}`, {
            headers: {
                accept: 'application/json',
                Authorization: `Bearer ${SessionStorageFunctions.getJwtTokenFromSessionStorage()}`
            }
        });
        return response.data;
    } catch (error) {
        return error;
    }
}