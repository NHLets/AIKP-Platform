import { Navigate, Route, Routes } from "react-router-dom";

import { MainLayout } from "../layouts";
import { CountryListPage } from "../modules/country";


export default function AppRouter() {
    return (
        
            <MainLayout>
                <Routes>
                    <Route
                        path="/countries"
                        element={<CountryListPage />}
                    />

                    <Route
                        path="*"
                        element={<Navigate to="/countries" replace />}
                    />
                </Routes>
            </MainLayout>
        
    );
}