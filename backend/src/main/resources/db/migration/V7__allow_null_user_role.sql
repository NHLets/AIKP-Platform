-- AIKP Platform
-- Allow user creation before role assignment

ALTER TABLE identity.users
ALTER COLUMN role_id DROP NOT NULL;
