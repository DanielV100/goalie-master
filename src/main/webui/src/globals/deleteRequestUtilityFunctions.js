import axios from "axios";
import * as UtilityFunctions from "@/globals/utilityFunctions";
import * as SessionStorageFunctions from "@/globals/sessionStorageUtilitiyFunctions";

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
        console.error(error);
        return [];
    }
}