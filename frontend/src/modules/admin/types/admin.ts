export interface UserSummary {
  id: string;
  username: string;
  email: string;
  fullName: string;
  role: string;
  organization: string;
  status: string;
}

export interface OrganizationSummary {
  id: string;
  name: string;
  type: string;
  country: string;
  status: string;
}

export interface PersonSummary {
  id: string;
  fullName: string;
  position: string;
  organization: string;
  email: string;
  status: string;
}
