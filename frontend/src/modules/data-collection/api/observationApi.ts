import axios from "axios";

const api = axios.create({
  baseURL: "/api/v1/observations"
});

export interface CreateObservationRequest{
  dataCollectionId:string;
  variableId:string;
  organizationId:string;
  value:string;
}

export const observationApi = {

  async createObservation(request:CreateObservationRequest){
    const {data} = await api.post("", request);
    return data;
  },

  async getObservationsByCollection(id:string){
    const {data} = await api.get(`/collection/${id}`);
    return data;
  }

};
