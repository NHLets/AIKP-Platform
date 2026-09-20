-- AIKP Data Collection 2026
-- Fiscal Template G: Financial data of public operators
-- Source: F_G.xlsx / Fiscal Template G
-- 53 variables: F130-F182
-- Reference years: 2022-2005

INSERT INTO metadata.questionnaire
(
    id,
    code,
    name,
    description,
    questionnaire_version,
    default_language,
    status,
    render_type,
    active,
    created_at,
    version
)
VALUES
('a4912808-3471-5204-9912-530955ac7f95', 'F_G', 'Fiscal Data Template G. Financial data of public operators', 'Fiscal Data Template G. Financial data of public operators', 1, 'en', 'ACTIVE', 'SPREADSHEET', TRUE, CURRENT_TIMESTAMP, 0);

INSERT INTO metadata.questionnaire_group
(
    id,
    questionnaire_id,
    parent_group_id,
    code,
    name,
    description,
    group_type,
    display_order,
    active,
    created_at,
    version
)
VALUES
('5d55281a-f1b7-5c42-aaa4-d6ba7e7d31f9', 'a4912808-3471-5204-9912-530955ac7f95', NULL, 'INCOME_STATEMENT', 'Income Statement', NULL, 'SECTION', 1, TRUE, CURRENT_TIMESTAMP, 0),
('aee0b43e-145b-53b8-b1b2-41e59b467ed9', 'a4912808-3471-5204-9912-530955ac7f95', NULL, 'CASH_FLOW', 'Statement of Cash Flows', NULL, 'SECTION', 2, TRUE, CURRENT_TIMESTAMP, 0),
('958556e4-e694-5072-acd8-6b8d4510dcc5', 'a4912808-3471-5204-9912-530955ac7f95', NULL, 'BALANCE_SHEET', 'Balance Sheet', NULL, 'SECTION', 3, TRUE, CURRENT_TIMESTAMP, 0);

INSERT INTO metadata.questionnaire_variable
(
    id,
    questionnaire_id,
    questionnaire_group_id,
    series_code,
    name,
    definition,
    data_type,
    unit,
    required,
    display_order,
    active,
    created_at,
    version
)
VALUES
('cad8640e-89d2-554b-ad8c-7d2260560240', 'a4912808-3471-5204-9912-530955ac7f95', '5d55281a-f1b7-5c42-aaa4-d6ba7e7d31f9', 'F130', 'Revenues from sales', 'The total revenue the public corporation has received from the sales of the services produced. In the case of special funds revenues may include levies, sector-specific taxes etc.', 'NUMBER', 'LCU per year', TRUE, 1, TRUE, CURRENT_TIMESTAMP, 0),
('ed658c38-3ea7-5553-b68d-14e32a6837d6', 'a4912808-3471-5204-9912-530955ac7f95', '5d55281a-f1b7-5c42-aaa4-d6ba7e7d31f9', 'F131', 'Total employee compensation', 'Total wages and social contributions paid to the workers etc. for delivering the services.', 'NUMBER', 'LCU per year', TRUE, 2, TRUE, CURRENT_TIMESTAMP, 0),
('ad698f5d-0845-54ec-9cc4-f499fc309c3e', 'a4912808-3471-5204-9912-530955ac7f95', '5d55281a-f1b7-5c42-aaa4-d6ba7e7d31f9', 'F132', 'Purchase of goods and services directly used in production ', 'The corporation''s purchase of goods and services necessary to produce the services delivered.', 'NUMBER', 'LCU per year', TRUE, 3, TRUE, CURRENT_TIMESTAMP, 0),
('792b6b12-773c-5fdc-8415-e0dde61cfba8', 'a4912808-3471-5204-9912-530955ac7f95', '5d55281a-f1b7-5c42-aaa4-d6ba7e7d31f9', 'F133', 'of which fuel', 'Expenditures by the public corporation on the purchase of electricity, oil or other fuel inputs', 'NUMBER', 'LCU per year', TRUE, 4, TRUE, CURRENT_TIMESTAMP, 0),
('6598b542-6dbc-5e82-a459-4e66c100f45c', 'a4912808-3471-5204-9912-530955ac7f95', '5d55281a-f1b7-5c42-aaa4-d6ba7e7d31f9', 'F134', 'of which Power Purchase Agreement (PPA) fees (if applicable)', 'Expenditures on Power Purchase Agreements (PPAs)', 'NUMBER', 'LCU per year', TRUE, 5, TRUE, CURRENT_TIMESTAMP, 0),
('0b478d03-192f-567b-834c-c4dc9b04629e', 'a4912808-3471-5204-9912-530955ac7f95', '5d55281a-f1b7-5c42-aaa4-d6ba7e7d31f9', 'F135', 'Other purchase of goods and services (i.e. those not included in line 3 above )', 'The corporation''s purchase of goods and services other than those necessary to produce the services delivered.', 'NUMBER', 'LCU per year', TRUE, 6, TRUE, CURRENT_TIMESTAMP, 0),
('92e4ae9e-fdcd-5137-95d3-fad9a59fa741', 'a4912808-3471-5204-9912-530955ac7f95', '5d55281a-f1b7-5c42-aaa4-d6ba7e7d31f9', 'F136', 'Rent', 'The rent paid to the owner of assets enabling the public corporation to produce the services.', 'NUMBER', 'LCU per year', TRUE, 7, TRUE, CURRENT_TIMESTAMP, 0),
('15df0f7a-7d06-5aa6-a21c-208b0220fd5c', 'a4912808-3471-5204-9912-530955ac7f95', '5d55281a-f1b7-5c42-aaa4-d6ba7e7d31f9', 'F137', 'Depreciation & amortisation', 'The amount of depreciation and amortization which the public corporation has deducted for the year. Depreciation and amortization are the terms used for the systematic allocation of the capitalized cost of an asset to income over its useful life. Strictly speaking, depreciation represents the allocation of the cost of tangible fixed assets, amortization refers to the cost of intangible assets,', 'NUMBER', 'LCU per year', TRUE, 8, TRUE, CURRENT_TIMESTAMP, 0),
('aa24d0e3-0f4d-53d9-95ee-d0dff05d1b30', 'a4912808-3471-5204-9912-530955ac7f95', '5d55281a-f1b7-5c42-aaa4-d6ba7e7d31f9', 'F138', 'Misc. taxes/fees (property etc.)', 'Various taxes (though not profit taxes) which the public corporation has to pay.', 'NUMBER', 'LCU per year', TRUE, 9, TRUE, CURRENT_TIMESTAMP, 0),
('85fa2d95-2100-5885-8ca3-dcdec8278108', 'a4912808-3471-5204-9912-530955ac7f95', '5d55281a-f1b7-5c42-aaa4-d6ba7e7d31f9', 'F139', 'Other operating expenditures', 'Other expenditures which the public corporation has incurred and which are not captured above, if any.', 'NUMBER', 'LCU per year', TRUE, 10, TRUE, CURRENT_TIMESTAMP, 0),
('1dd69a5f-96bc-51c6-860d-0a7de1b5b16f', 'a4912808-3471-5204-9912-530955ac7f95', '5d55281a-f1b7-5c42-aaa4-d6ba7e7d31f9', 'F140', 'Income (loss) from operations', 'Income or loss from operations calculated as a difference between operational revenues and operational expenditures. ', 'NUMBER', 'LCU per year', TRUE, 11, TRUE, CURRENT_TIMESTAMP, 0),
('97969fac-1dfd-5e98-9ddd-c9f18085942d', 'a4912808-3471-5204-9912-530955ac7f95', '5d55281a-f1b7-5c42-aaa4-d6ba7e7d31f9', 'F141', 'Interest paid', 'Interest (on both domestic and foreign debt) which the public corporation has to pay on its debt.', 'NUMBER', 'LCU per year', TRUE, 12, TRUE, CURRENT_TIMESTAMP, 0),
('7f597811-cbef-5703-8814-d281423e0cb2', 'a4912808-3471-5204-9912-530955ac7f95', '5d55281a-f1b7-5c42-aaa4-d6ba7e7d31f9', 'F142', 'Foreign Interest Paid (of which, foreign)', NULL, 'NUMBER', 'LCU per year', TRUE, 13, TRUE, CURRENT_TIMESTAMP, 0),
('c0352823-38c1-5803-b05e-2693c9c8e1c9', 'a4912808-3471-5204-9912-530955ac7f95', '5d55281a-f1b7-5c42-aaa4-d6ba7e7d31f9', 'F143', 'Interest earned', 'The interest which the public corporation has received during the year on either its financial investments or its cash balance.', 'NUMBER', 'LCU per year', TRUE, 14, TRUE, CURRENT_TIMESTAMP, 0),
('fcee4e01-0977-5eee-9aea-4d3855145daf', 'a4912808-3471-5204-9912-530955ac7f95', '5d55281a-f1b7-5c42-aaa4-d6ba7e7d31f9', 'F144', 'Direct foreign grants', 'Foreign grants which the public corporation has received but which have not been posted on the central government budget or local government budget.', 'NUMBER', 'LCU per year', TRUE, 15, TRUE, CURRENT_TIMESTAMP, 0),
('ed3afde9-01a8-5b2e-a937-82e5e1068155', 'a4912808-3471-5204-9912-530955ac7f95', '5d55281a-f1b7-5c42-aaa4-d6ba7e7d31f9', 'F145', 'Transfers/subsidies from government', 'The subsidies which the public corporation has received from the local or general government for supporting service delivery.', 'NUMBER', 'LCU per year', TRUE, 16, TRUE, CURRENT_TIMESTAMP, 0),
('c6c22354-0557-5053-a90b-1cdf534c13ff', 'a4912808-3471-5204-9912-530955ac7f95', '5d55281a-f1b7-5c42-aaa4-d6ba7e7d31f9', 'F146', 'Revenue from irregular activities', 'Revenue produced by activities that are not part of the regular company operations.', 'NUMBER', 'LCU per year', TRUE, 17, TRUE, CURRENT_TIMESTAMP, 0),
('d33f66ea-a2f1-5255-86d2-019c61286255', 'a4912808-3471-5204-9912-530955ac7f95', '5d55281a-f1b7-5c42-aaa4-d6ba7e7d31f9', 'F147', 'of which, Fixed Assets'' selling price', 'Revenue received from the sale of Property, Plant, and Equipment, if any.', 'NUMBER', 'LCU per year', TRUE, 18, TRUE, CURRENT_TIMESTAMP, 0),
('6cd07580-6a73-5d7d-a993-5f85c0da53b8', 'a4912808-3471-5204-9912-530955ac7f95', '5d55281a-f1b7-5c42-aaa4-d6ba7e7d31f9', 'F148', 'Other non-operating revenue', 'Other non-operating revenue which the public corporation has earned but which are not included above.', 'NUMBER', 'LCU per year', TRUE, 19, TRUE, CURRENT_TIMESTAMP, 0),
('d6464f79-4fbc-5ee5-9ddc-c5d26cf89498', 'a4912808-3471-5204-9912-530955ac7f95', '5d55281a-f1b7-5c42-aaa4-d6ba7e7d31f9', 'F149', 'Irregular activities expenditures', 'Expenses incurred because of activities that are not part of the regular company operations.', 'NUMBER', 'LCU per year', TRUE, 20, TRUE, CURRENT_TIMESTAMP, 0),
('2ba007e1-3349-5f20-87b3-22540c34cdb4', 'a4912808-3471-5204-9912-530955ac7f95', '5d55281a-f1b7-5c42-aaa4-d6ba7e7d31f9', 'F150', 'of which, Book value of Fixed Assets sold', 'Book value of Property, Plant, and Equipment sold, if any.', 'NUMBER', 'LCU per year', TRUE, 21, TRUE, CURRENT_TIMESTAMP, 0),
('bf0b2827-9c0d-5719-ba4c-cf8cdf6cfa0d', 'a4912808-3471-5204-9912-530955ac7f95', '5d55281a-f1b7-5c42-aaa4-d6ba7e7d31f9', 'F151', 'Other non-operating expenditures', 'Other non-operating expenses which the public corporation has incurred but which are not included above.', 'NUMBER', 'LCU per year', TRUE, 22, TRUE, CURRENT_TIMESTAMP, 0),
('06127797-a4fb-5d0b-9daa-1ffdc448ac7d', 'a4912808-3471-5204-9912-530955ac7f95', '5d55281a-f1b7-5c42-aaa4-d6ba7e7d31f9', 'F152', 'Profit (loss) before tax', 'The profit or loss before income tax calculated as difference between total revenues and total expenditures.', 'NUMBER', 'LCU per year', TRUE, 23, TRUE, CURRENT_TIMESTAMP, 0),
('9e900724-77b0-53b4-a241-fcd2b9b8c42b', 'a4912808-3471-5204-9912-530955ac7f95', '5d55281a-f1b7-5c42-aaa4-d6ba7e7d31f9', 'F153', 'Corporate income tax', 'The corporate income tax or profit tax.', 'NUMBER', 'LCU per year', TRUE, 24, TRUE, CURRENT_TIMESTAMP, 0),
('aab4fefa-bafa-5266-87a2-b8302f57b4e8', 'a4912808-3471-5204-9912-530955ac7f95', '5d55281a-f1b7-5c42-aaa4-d6ba7e7d31f9', 'F154', '(of which, tax exemptions)', 'Income tax exemptions, if any ', 'NUMBER', 'LCU per year', TRUE, 25, TRUE, CURRENT_TIMESTAMP, 0),
('b737bc3f-15ae-58cc-9c52-5abc77f7b58f', 'a4912808-3471-5204-9912-530955ac7f95', '5d55281a-f1b7-5c42-aaa4-d6ba7e7d31f9', 'F155', 'Net income, as reported by enterprise', 'The net profit or net earnings (profit after tax) of the public corporation for the accounting period, as reported in the financial statement.', 'NUMBER', 'LCU per year', TRUE, 26, TRUE, CURRENT_TIMESTAMP, 0),
('faa8b747-16c1-5eee-9b83-11fe1739075d', 'a4912808-3471-5204-9912-530955ac7f95', 'aee0b43e-145b-53b8-b1b2-41e59b467ed9', 'F156', 'Net cash from operating activities', 'is defined as net amount of cash provided from operating activities. Operating activities include the company''s day-to-day activities that create revenues, such as selling inventory and providing services. Cash inflows result from cash sales and from collection of accounts receivable. Examples include cash receipts from the provision of services and other revenue. Cash outflows result from cash payments for inventory, salaries, taxes, and other operating-related expenses and from paying accounts payable.', 'NUMBER', 'LCU per year', TRUE, 27, TRUE, CURRENT_TIMESTAMP, 0),
('86584686-5896-5c03-b466-92f5c16c2bf8', 'a4912808-3471-5204-9912-530955ac7f95', 'aee0b43e-145b-53b8-b1b2-41e59b467ed9', 'F157', 'Net cash flow from investing activities', 'is defined as net amount of cash provided from investing activities. Investing activities include purchase and selling investments. Investments include property, plant and equipment; intangible assets; other long-term assets; and both long-term and short-term investments in the equity and debt (bonds and loans) issued by other companies. Cash flows in the investing category include cash receipts from the sale of not-trading securities, property, plant, and equipment; intangibles or other long-term assets. Cash outflows include cash payments for the purchase of these assets.', 'NUMBER', 'LCU per year', TRUE, 28, TRUE, CURRENT_TIMESTAMP, 0),
('f03167f8-a7de-5c24-a451-30adeaedcb66', 'a4912808-3471-5204-9912-530955ac7f95', 'aee0b43e-145b-53b8-b1b2-41e59b467ed9', 'F158', 'Capitalized rehabilitation costs (increase in the period)', 'is defined as capitalized rehabilitation costs. This cost is depreciated over the life of the rehabilitated asset instead of being expensed immediately.  As an outflow, this item must be entered with a positive sign into the Statement of cash flows template.', 'NUMBER', 'LCU per year', TRUE, 29, TRUE, CURRENT_TIMESTAMP, 0),
('db20edad-ccc7-54ee-be44-f6fa3a064b5b', 'a4912808-3471-5204-9912-530955ac7f95', 'aee0b43e-145b-53b8-b1b2-41e59b467ed9', 'F159', 'Purchase of intangible assets', 'Cash payments for the purchase of intangible assets. Intangible assets are not physical in nature. They include corporate intellectual property (patents, trademarks, copyrights, business methodologies), goodwill and brand recognition. In the case of utilities and telecom service providers, intangible assets also include billing data, contextual information and analytics, credit history and social networking interests.', 'NUMBER', 'LCU per year', TRUE, 30, TRUE, CURRENT_TIMESTAMP, 0),
('7a5b6ec4-44d0-5c94-bd11-c7294c7382db', 'a4912808-3471-5204-9912-530955ac7f95', 'aee0b43e-145b-53b8-b1b2-41e59b467ed9', 'F160', 'Purchase of property, plant, and equipment', 'Cash outflows for purchase of tangible assets (i.e. property, plant and equipment). As an outflow, this item must be entered with a positive sign into the Statement of cash flows template.', 'NUMBER', 'LCU per year', TRUE, 31, TRUE, CURRENT_TIMESTAMP, 0),
('3ca0f8c4-1d94-5758-a0b5-8cd35dc2e627', 'a4912808-3471-5204-9912-530955ac7f95', 'aee0b43e-145b-53b8-b1b2-41e59b467ed9', 'F161', '(of which replacement of property, plant and equipment)', 'Cash outflows used for replacement of existing tangible assets (subset of the entry above), if available. As an outflow, this item must be entered with a positive sign into the Statement of cash flows template.', 'NUMBER', 'LCU per year', TRUE, 32, TRUE, CURRENT_TIMESTAMP, 0),
('e3ea6016-9c04-5a97-9638-11dd797d0f5e', 'a4912808-3471-5204-9912-530955ac7f95', 'aee0b43e-145b-53b8-b1b2-41e59b467ed9', 'F162', 'Sales of property, plant, and equipment, if any', 'Cash inflows from the sale of property, plant, and equipment. As an inflow, this item must be entered with a negative sign into the Statement of cash flows template.', 'NUMBER', 'LCU per year', TRUE, 33, TRUE, CURRENT_TIMESTAMP, 0),
('a58e77dc-6a33-5109-a018-850bc366d325', 'a4912808-3471-5204-9912-530955ac7f95', 'aee0b43e-145b-53b8-b1b2-41e59b467ed9', 'F163', 'Purchase of financial investing assets', 'Cash payments for the purchase of long-term and short-term investments in the equity and debt (bonds and loans) issued by other companies', 'NUMBER', 'LCU per year', TRUE, 34, TRUE, CURRENT_TIMESTAMP, 0),
('2fa40e0f-430d-5ec6-9a0a-3c9225386ee8', 'a4912808-3471-5204-9912-530955ac7f95', 'aee0b43e-145b-53b8-b1b2-41e59b467ed9', 'F164', 'Net cash flow from financing activities', 'is defined as net amount of cash provided from financing activities. Financing activities include obtaining or repaying capital, such as equity and long-term debt. The two primary sources of capital are shareholders and creditors. Cash inflows in this category include cash receipts from issuing stock or bonds and cash receipts from borrowing. Cash outflows include cash payments to repurchase stock, to pay dividends, and to repay bonds and other borrowings.', 'NUMBER', 'LCU per year', TRUE, 35, TRUE, CURRENT_TIMESTAMP, 0),
('cbfe2a11-8ad1-56b4-ab42-59f1840a7d4b', 'a4912808-3471-5204-9912-530955ac7f95', 'aee0b43e-145b-53b8-b1b2-41e59b467ed9', 'F165', 'Dividends paid', 'Cash paid in dividends to the company shareholders. Could be inflow or outflow, depending whether dividends were paid or retained.', 'NUMBER', 'LCU per year', TRUE, 36, TRUE, CURRENT_TIMESTAMP, 0),
('0702c231-2181-5f8d-b386-388b8702a147', 'a4912808-3471-5204-9912-530955ac7f95', 'aee0b43e-145b-53b8-b1b2-41e59b467ed9', 'F166', '(of which, to government)', 'Dividends paid to government.', 'NUMBER', 'LCU per year', TRUE, 37, TRUE, CURRENT_TIMESTAMP, 0),
('1f7dce3e-60c1-5c89-aff6-c432269192f8', 'a4912808-3471-5204-9912-530955ac7f95', 'aee0b43e-145b-53b8-b1b2-41e59b467ed9', 'F167', 'Investment grants received', 'Includes resources obtained to finance new investments: grants from government, foreign grants, foreign and domestic loans, issuance of new shares and bonds. As an inflow, this item must be entered with a negative sign into the Statement of cash flows template.', 'NUMBER', 'LCU per year', TRUE, 38, TRUE, CURRENT_TIMESTAMP, 0),
('edd3fce6-bacc-5853-9be8-ec148c62faa7', 'a4912808-3471-5204-9912-530955ac7f95', 'aee0b43e-145b-53b8-b1b2-41e59b467ed9', 'F168', 'New loans ', 'New loans received.', 'NUMBER', 'LCU per year', TRUE, 39, TRUE, CURRENT_TIMESTAMP, 0),
('d399c85c-065a-5886-9a96-39b9b67c66af', 'a4912808-3471-5204-9912-530955ac7f95', '958556e4-e694-5072-acd8-6b8d4510dcc5', 'F169', 'Current Assets', 'The current assets of the public corporation. The current assets are the cash deposits, trade receivables, inventories, accounts receivable, etc.', 'NUMBER', 'LCU per year', TRUE, 40, TRUE, CURRENT_TIMESTAMP, 0),
('a3a4d272-777e-5c71-8249-f8c9bc159b66', 'a4912808-3471-5204-9912-530955ac7f95', '958556e4-e694-5072-acd8-6b8d4510dcc5', 'F170', 'Noncurrent Assets', 'The fixed and other assets that the public corporation has acquired at the cost price.', 'NUMBER', 'LCU per year', TRUE, 41, TRUE, CURRENT_TIMESTAMP, 0),
('7924ac6e-bee9-509f-b27c-85099de1cd94', 'a4912808-3471-5204-9912-530955ac7f95', '958556e4-e694-5072-acd8-6b8d4510dcc5', 'F171', 'Gross value of capitalized rehabilitation costs', 'Capitalized or deferred rehabilitation costs', 'NUMBER', 'LCU per year', TRUE, 42, TRUE, CURRENT_TIMESTAMP, 0),
('f57547b3-51a4-5234-929f-dd53c2878bc0', 'a4912808-3471-5204-9912-530955ac7f95', '958556e4-e694-5072-acd8-6b8d4510dcc5', 'F172', 'Depreciation & Amortization accumulated on deferred rehabilitation costs', 'The accumulated depreciation on capitalized rehabilitation costs.', 'NUMBER', 'LCU per year', TRUE, 43, TRUE, CURRENT_TIMESTAMP, 0),
('85c7a228-fb7f-505e-8f21-ce46de8bb27f', 'a4912808-3471-5204-9912-530955ac7f95', '958556e4-e694-5072-acd8-6b8d4510dcc5', 'F173', 'Gross value of propery, plant, and equipment', 'Gross value of Property, Plant, and Equipment (i.e. before any depreciation expenditure)', 'NUMBER', 'LCU per year', TRUE, 44, TRUE, CURRENT_TIMESTAMP, 0),
('dbf9de5e-82ea-50ca-a778-9292a767dc49', 'a4912808-3471-5204-9912-530955ac7f95', '958556e4-e694-5072-acd8-6b8d4510dcc5', 'F174', 'Depreciation & Amortization accumulated on property, plant, and equipment', 'The accumulated depreciation on Property, Plant, and Equipment.', 'NUMBER', 'LCU per year', TRUE, 45, TRUE, CURRENT_TIMESTAMP, 0),
('3bd6faa6-8e2b-5c13-b576-9d708012bf06', 'a4912808-3471-5204-9912-530955ac7f95', '958556e4-e694-5072-acd8-6b8d4510dcc5', 'F175', 'Total Assets', 'Sum of current and noncurrent assets', 'NUMBER', 'LCU per year', TRUE, 46, TRUE, CURRENT_TIMESTAMP, 0),
('a708b20f-178f-5409-9d1e-4ceb457245bf', 'a4912808-3471-5204-9912-530955ac7f95', '958556e4-e694-5072-acd8-6b8d4510dcc5', 'F176', 'Current Liabilities', 'The public corporation''s current liabilities, which is the sum of the accounts payable, deferred taxation, etc.', 'NUMBER', 'LCU per year', TRUE, 47, TRUE, CURRENT_TIMESTAMP, 0),
('47e84e1d-05be-5cfe-93d5-c82973a7cd69', 'a4912808-3471-5204-9912-530955ac7f95', '958556e4-e694-5072-acd8-6b8d4510dcc5', 'F177', '(of which foreign)', 'This specifies if any of the public corporation''s current liabilities are to be paid to entities abroad.', 'NUMBER', 'LCU per year', TRUE, 48, TRUE, CURRENT_TIMESTAMP, 0),
('24d46638-e7bf-5a1c-8482-8354d4a5a856', 'a4912808-3471-5204-9912-530955ac7f95', '958556e4-e694-5072-acd8-6b8d4510dcc5', 'F178', 'Long Term Liabilities', 'The public corporation''s long term liabilities, i.e. the long-term debt of the public corporation.', 'NUMBER', 'LCU per year', TRUE, 49, TRUE, CURRENT_TIMESTAMP, 0),
('51c2fa30-6c26-598f-ade6-cfdb5d5c802a', 'a4912808-3471-5204-9912-530955ac7f95', '958556e4-e694-5072-acd8-6b8d4510dcc5', 'F179', '(of which foreign)', 'The portion of the long-term debt which is foreign debt.', 'NUMBER', 'LCU per year', TRUE, 50, TRUE, CURRENT_TIMESTAMP, 0),
('3202304f-5c76-5f8c-9d58-aa661070ae5a', 'a4912808-3471-5204-9912-530955ac7f95', '958556e4-e694-5072-acd8-6b8d4510dcc5', 'F180', 'Equity and Reserves', 'The public corporation''s equity and reserves.', 'NUMBER', 'LCU per year', TRUE, 51, TRUE, CURRENT_TIMESTAMP, 0),
('8bbb26ba-b0da-5d07-a2f0-2e202941cd29', 'a4912808-3471-5204-9912-530955ac7f95', '958556e4-e694-5072-acd8-6b8d4510dcc5', 'F181', 'Retained earnings (retained deficit) for the period', 'Cumulative earnings retained in the company.', 'NUMBER', 'LCU per year', TRUE, 52, TRUE, CURRENT_TIMESTAMP, 0),
('ec09bdfd-3dd7-55e4-9d3c-1589ecdd8872', 'a4912808-3471-5204-9912-530955ac7f95', '958556e4-e694-5072-acd8-6b8d4510dcc5', 'F182', 'Total liabilities & Equity', 'Sum of Liabilities and Equity', 'NUMBER', 'LCU per year', TRUE, 53, TRUE, CURRENT_TIMESTAMP, 0);
