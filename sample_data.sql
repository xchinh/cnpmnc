-- =============================================================================
-- CRM SAMPLE DATA - 20 RECORDS PER TABLE
-- =============================================================================

-- Xóa dữ liệu cũ (nếu có) theo thứ tự ngược với foreign key
TRUNCATE TABLE activities RESTART IDENTITY CASCADE;
TRUNCATE TABLE comments RESTART IDENTITY CASCADE;
TRUNCATE TABLE notes RESTART IDENTITY CASCADE;
TRUNCATE TABLE interactions RESTART IDENTITY CASCADE;
TRUNCATE TABLE customer_assignments RESTART IDENTITY CASCADE;
TRUNCATE TABLE customers RESTART IDENTITY CASCADE;
TRUNCATE TABLE team_members RESTART IDENTITY CASCADE;
TRUNCATE TABLE teams RESTART IDENTITY CASCADE;
TRUNCATE TABLE users RESTART IDENTITY CASCADE;

-- =============================================================================
-- 1. USERS TABLE (20 records)
-- =============================================================================
INSERT INTO users (email, password_hash, name, role, is_active, created_at, updated_at, deleted_at) VALUES
('admin@crm.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Nguyễn Văn Admin', 'ADMIN', true, NOW() - INTERVAL '180 days', NOW() - INTERVAL '10 days', NULL),
('manager1@crm.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Trần Thị Manager 1', 'MANAGER', true, NOW() - INTERVAL '170 days', NOW() - INTERVAL '5 days', NULL),
('manager2@crm.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Lê Văn Manager 2', 'MANAGER', true, NOW() - INTERVAL '160 days', NOW() - INTERVAL '3 days', NULL),
('editor1@crm.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Phạm Thị Editor 1', 'EDITOR', true, NOW() - INTERVAL '150 days', NOW() - INTERVAL '2 days', NULL),
('editor2@crm.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Hoàng Văn Editor 2', 'EDITOR', true, NOW() - INTERVAL '140 days', NOW() - INTERVAL '1 day', NULL),
('editor3@crm.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Đỗ Thị Editor 3', 'EDITOR', true, NOW() - INTERVAL '130 days', NOW(), NULL),
('editor4@crm.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Vũ Văn Editor 4', 'EDITOR', true, NOW() - INTERVAL '120 days', NOW(), NULL),
('editor5@crm.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Bùi Thị Editor 5', 'EDITOR', true, NOW() - INTERVAL '110 days', NOW(), NULL),
('viewer1@crm.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Đặng Văn Viewer 1', 'VIEWER', true, NOW() - INTERVAL '100 days', NOW(), NULL),
('viewer2@crm.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Dương Thị Viewer 2', 'VIEWER', true, NOW() - INTERVAL '90 days', NOW(), NULL),
('viewer3@crm.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Lý Văn Viewer 3', 'VIEWER', true, NOW() - INTERVAL '80 days', NOW(), NULL),
('sale1@crm.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Mai Thị Sale 1', 'EDITOR', true, NOW() - INTERVAL '70 days', NOW(), NULL),
('sale2@crm.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Phan Văn Sale 2', 'EDITOR', true, NOW() - INTERVAL '60 days', NOW(), NULL),
('sale3@crm.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Cao Thị Sale 3', 'EDITOR', true, NOW() - INTERVAL '50 days', NOW(), NULL),
('support1@crm.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Võ Văn Support 1', 'EDITOR', true, NOW() - INTERVAL '40 days', NOW(), NULL),
('support2@crm.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Tô Thị Support 2', 'EDITOR', true, NOW() - INTERVAL '30 days', NOW(), NULL),
('marketing1@crm.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Hồ Văn Marketing 1', 'EDITOR', true, NOW() - INTERVAL '20 days', NOW(), NULL),
('marketing2@crm.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Đinh Thị Marketing 2', 'VIEWER', true, NOW() - INTERVAL '15 days', NOW(), NULL),
('intern1@crm.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Tạ Văn Intern 1', 'VIEWER', true, NOW() - INTERVAL '10 days', NOW(), NULL),
('intern2@crm.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Lương Thị Intern 2', 'VIEWER', false, NOW() - INTERVAL '5 days', NOW(), NULL);

-- =============================================================================
-- 2. TEAMS TABLE (20 records)
-- =============================================================================
INSERT INTO teams (name, description, created_by, created_at, updated_at, deleted_at) VALUES
('Sales Team Hà Nội', 'Đội ngũ kinh doanh khu vực miền Bắc', 1, NOW() - INTERVAL '150 days', NOW() - INTERVAL '10 days', NULL),
('Sales Team TP.HCM', 'Đội ngũ kinh doanh khu vực miền Nam', 1, NOW() - INTERVAL '145 days', NOW() - INTERVAL '8 days', NULL),
('Sales Team Đà Nẵng', 'Đội ngũ kinh doanh khu vực miền Trung', 2, NOW() - INTERVAL '140 days', NOW() - INTERVAL '5 days', NULL),
('Customer Support Team', 'Đội hỗ trợ khách hàng 24/7', 1, NOW() - INTERVAL '135 days', NOW() - INTERVAL '3 days', NULL),
('Marketing Team', 'Đội Marketing & Communications', 2, NOW() - INTERVAL '130 days', NOW() - INTERVAL '2 days', NULL),
('Technical Support Team', 'Đội hỗ trợ kỹ thuật', 3, NOW() - INTERVAL '125 days', NOW(), NULL),
('Enterprise Sales Team', 'Đội bán hàng doanh nghiệp lớn', 2, NOW() - INTERVAL '120 days', NOW(), NULL),
('SME Sales Team', 'Đội bán hàng doanh nghiệp vừa và nhỏ', 2, NOW() - INTERVAL '115 days', NOW(), NULL),
('Retail Team', 'Đội bán lẻ trực tiếp', 3, NOW() - INTERVAL '110 days', NOW(), NULL),
('Partner Management Team', 'Quản lý đối tác chiến lược', 1, NOW() - INTERVAL '105 days', NOW(), NULL),
('Product Team', 'Đội phát triển sản phẩm', 1, NOW() - INTERVAL '100 days', NOW(), NULL),
('Customer Success Team', 'Đội chăm sóc thành công khách hàng', 2, NOW() - INTERVAL '95 days', NOW(), NULL),
('Business Development Team', 'Đội phát triển kinh doanh', 2, NOW() - INTERVAL '90 days', NOW(), NULL),
('Operations Team', 'Đội vận hành', 3, NOW() - INTERVAL '85 days', NOW(), NULL),
('Finance Team', 'Đội tài chính kế toán', 1, NOW() - INTERVAL '80 days', NOW(), NULL),
('HR Team', 'Đội nhân sự', 1, NOW() - INTERVAL '75 days', NOW(), NULL),
('IT Support Team', 'Đội hỗ trợ IT nội bộ', 3, NOW() - INTERVAL '70 days', NOW(), NULL),
('Quality Assurance Team', 'Đội kiểm soát chất lượng', 2, NOW() - INTERVAL '65 days', NOW(), NULL),
('Research Team', 'Đội nghiên cứu thị trường', 2, NOW() - INTERVAL '60 days', NOW(), NULL),
('Training Team', 'Đội đào tạo nội bộ', 1, NOW() - INTERVAL '55 days', NOW(), NULL);

-- =============================================================================
-- 3. TEAM_MEMBERS TABLE (20 records)
-- =============================================================================
INSERT INTO team_members (team_id, user_id, team_role, joined_at, updated_at) VALUES
(1, 2, 'MANAGER', NOW() - INTERVAL '140 days', NOW()),
(1, 4, 'EDITOR', NOW() - INTERVAL '135 days', NOW()),
(1, 12, 'EDITOR', NOW() - INTERVAL '60 days', NOW()),
(2, 3, 'MANAGER', NOW() - INTERVAL '130 days', NOW()),
(2, 5, 'EDITOR', NOW() - INTERVAL '125 days', NOW()),
(2, 13, 'EDITOR', NOW() - INTERVAL '55 days', NOW()),
(3, 2, 'MANAGER', NOW() - INTERVAL '120 days', NOW()),
(3, 6, 'EDITOR', NOW() - INTERVAL '115 days', NOW()),
(3, 14, 'EDITOR', NOW() - INTERVAL '45 days', NOW()),
(4, 3, 'MANAGER', NOW() - INTERVAL '110 days', NOW()),
(4, 15, 'EDITOR', NOW() - INTERVAL '35 days', NOW()),
(4, 16, 'EDITOR', NOW() - INTERVAL '28 days', NOW()),
(5, 2, 'MANAGER', NOW() - INTERVAL '105 days', NOW()),
(5, 17, 'EDITOR', NOW() - INTERVAL '18 days', NOW()),
(5, 18, 'VIEWER', NOW() - INTERVAL '14 days', NOW()),
(6, 3, 'MANAGER', NOW() - INTERVAL '100 days', NOW()),
(7, 2, 'MANAGER', NOW() - INTERVAL '95 days', NOW()),
(8, 3, 'MANAGER', NOW() - INTERVAL '90 days', NOW()),
(9, 2, 'MANAGER', NOW() - INTERVAL '85 days', NOW()),
(10, 1, 'ADMIN', NOW() - INTERVAL '80 days', NOW());

-- =============================================================================
-- 4. CUSTOMERS TABLE (20 records)
-- =============================================================================
INSERT INTO customers (name, email, phone, company, notes, profile_picture, team_id, created_by, created_at, updated_at, deleted_at, version) VALUES
('Nguyễn Văn A', 'nguyenvana@techcorp.vn', '0901234567', 'TechCorp Vietnam', 'Khách hàng tiềm năng, quan tâm đến giải pháp Enterprise', NULL, 1, 4, NOW() - INTERVAL '120 days', NOW() - INTERVAL '2 days', NULL, 0),
('Trần Thị B', 'tranthib@innovate.com.vn', '0902345678', 'Innovate Solutions', 'Đã mua gói Basic, đang cân nhắc nâng cấp', NULL, 1, 4, NOW() - INTERVAL '110 days', NOW() - INTERVAL '5 days', NULL, 0),
('Lê Văn C', 'levanc@smarttech.vn', '0903456789', 'SmartTech JSC', 'Khách hàng VIP, cần chăm sóc đặc biệt', NULL, 2, 5, NOW() - INTERVAL '100 days', NOW() - INTERVAL '1 day', NULL, 0),
('Phạm Thị D', 'phamthid@digitalvn.com', '0904567890', 'Digital Vietnam', 'Khách hàng mới, trial period', NULL, 2, 5, NOW() - INTERVAL '90 days', NOW(), NULL, 0),
('Hoàng Văn E', 'hoangvane@futuresoft.vn', '0905678901', 'FutureSoft Co.', 'Đã kí hợp đồng 1 năm', NULL, 3, 6, NOW() - INTERVAL '85 days', NOW(), NULL, 0),
('Đỗ Thị F', 'dothif@cloudnet.vn', '0906789012', 'CloudNet Systems', 'Cần hỗ trợ kỹ thuật thường xuyên', NULL, 3, 6, NOW() - INTERVAL '80 days', NOW(), NULL, 0),
('Vũ Văn G', 'vuvang@bizcom.vn', '0907890123', 'BizCom Vietnam', 'Khách hàng từ referral', NULL, 1, 12, NOW() - INTERVAL '75 days', NOW(), NULL, 0),
('Bùi Thị H', 'buithih@enterprise.com.vn', '0908901234', 'Enterprise Plus', 'Enterprise customer - high value', NULL, 7, 4, NOW() - INTERVAL '70 days', NOW(), NULL, 0),
('Đặng Văn I', 'dangvani@retailpro.vn', '0909012345', 'RetailPro Vietnam', 'Khách hàng bán lẻ', NULL, 9, 6, NOW() - INTERVAL '65 days', NOW(), NULL, 0),
('Dương Thị K', 'duongthik@logistics.vn', '0900123456', 'Vietnam Logistics', 'Đang đàm phán hợp đồng mới', NULL, 2, 13, NOW() - INTERVAL '60 days', NOW(), NULL, 0),
('Lý Văn L', 'lyvanl@manufacturing.vn', '0911234567', 'Manufacturing Co.', 'Khách hàng sản xuất lớn', NULL, 7, 5, NOW() - INTERVAL '55 days', NOW(), NULL, 0),
('Mai Thị M', 'maithim@education.vn', '0912345678', 'Education Hub', 'Khách hàng giáo dục - special discount', NULL, 8, 13, NOW() - INTERVAL '50 days', NOW(), NULL, 0),
('Phan Văn N', 'phanvann@healthcare.vn', '0913456789', 'Healthcare Solutions', 'Y tế - yêu cầu bảo mật cao', NULL, 4, 15, NOW() - INTERVAL '45 days', NOW(), NULL, 0),
('Cao Thị O', 'caothio@finance.vn', '0914567890', 'Finance Corp', 'Tài chính - compliance requirements', NULL, 7, 4, NOW() - INTERVAL '40 days', NOW(), NULL, 0),
('Võ Văn P', 'vovanp@travel.vn', '0915678901', 'Travel Vietnam', 'Du lịch - seasonal customer', NULL, 2, 5, NOW() - INTERVAL '35 days', NOW(), NULL, 0),
('Tô Thị Q', 'tothiq@food.vn', '0916789012', 'Food & Beverage JSC', 'F&B - nhiều chi nhánh', NULL, 8, 14, NOW() - INTERVAL '30 days', NOW(), NULL, 0),
('Hồ Văn R', 'hovanr@realestate.vn', '0917890123', 'Real Estate Group', 'Bất động sản - customer lớn', NULL, 7, 12, NOW() - INTERVAL '25 days', NOW(), NULL, 0),
('Đinh Thị S', 'dinhthis@media.vn', '0918901234', 'Media Network', 'Truyền thông - content focus', NULL, 5, 17, NOW() - INTERVAL '20 days', NOW(), NULL, 0),
('Tạ Văn T', 'tavant@agriculture.vn', '0919012345', 'Agriculture Tech', 'Nông nghiệp công nghệ cao', NULL, 8, 14, NOW() - INTERVAL '15 days', NOW(), NULL, 0),
('Lương Thị U', 'luongthiu@ecommerce.vn', '0920123456', 'E-Commerce Hub', 'Thương mại điện tử', NULL, 2, 13, NOW() - INTERVAL '10 days', NOW(), NULL, 0);

-- =============================================================================
-- 5. CUSTOMER_ASSIGNMENTS TABLE (20 records)
-- =============================================================================
INSERT INTO customer_assignments (customer_id, user_id, assignment_type, assigned_by, assigned_at) VALUES
(1, 4, 'OWNER', 2, NOW() - INTERVAL '120 days'),
(1, 12, 'COLLABORATOR', 2, NOW() - INTERVAL '60 days'),
(2, 4, 'OWNER', 2, NOW() - INTERVAL '110 days'),
(3, 5, 'OWNER', 3, NOW() - INTERVAL '100 days'),
(3, 13, 'COLLABORATOR', 3, NOW() - INTERVAL '50 days'),
(4, 5, 'OWNER', 3, NOW() - INTERVAL '90 days'),
(5, 6, 'OWNER', 2, NOW() - INTERVAL '85 days'),
(6, 6, 'OWNER', 2, NOW() - INTERVAL '80 days'),
(7, 12, 'OWNER', 2, NOW() - INTERVAL '75 days'),
(8, 4, 'OWNER', 2, NOW() - INTERVAL '70 days'),
(9, 6, 'OWNER', 2, NOW() - INTERVAL '65 days'),
(10, 13, 'OWNER', 3, NOW() - INTERVAL '60 days'),
(11, 5, 'OWNER', 3, NOW() - INTERVAL '55 days'),
(12, 13, 'OWNER', 3, NOW() - INTERVAL '50 days'),
(13, 15, 'OWNER', 3, NOW() - INTERVAL '45 days'),
(14, 4, 'OWNER', 2, NOW() - INTERVAL '40 days'),
(15, 5, 'OWNER', 3, NOW() - INTERVAL '35 days'),
(16, 14, 'OWNER', 3, NOW() - INTERVAL '30 days'),
(17, 12, 'OWNER', 2, NOW() - INTERVAL '25 days'),
(18, 17, 'OWNER', 2, NOW() - INTERVAL '20 days');

-- =============================================================================
-- 6. INTERACTIONS TABLE (20 records)
-- =============================================================================
INSERT INTO interactions (customer_id, user_id, type, description, interaction_date, created_at, updated_at, deleted_at) VALUES
(1, 4, 'MEETING', 'Họp giới thiệu sản phẩm và tính năng Enterprise. Khách hàng rất quan tâm.', NOW() - INTERVAL '118 days', NOW() - INTERVAL '118 days', NOW(), NULL),
(1, 4, 'EMAIL', 'Gửi quotation và proposal. Chờ phản hồi.', NOW() - INTERVAL '115 days', NOW() - INTERVAL '115 days', NOW(), NULL),
(1, 12, 'CALL', 'Follow-up về proposal. Khách hàng cần thêm thời gian để xem xét.', NOW() - INTERVAL '110 days', NOW() - INTERVAL '110 days', NOW(), NULL),
(2, 4, 'CALL', 'Tư vấn nâng cấp lên gói Premium. Khách hàng quan tâm các tính năng mới.', NOW() - INTERVAL '105 days', NOW() - INTERVAL '105 days', NOW(), NULL),
(2, 4, 'MEETING', 'Demo các tính năng Premium. Presentation thành công.', NOW() - INTERVAL '100 days', NOW() - INTERVAL '100 days', NOW(), NULL),
(3, 5, 'MEETING', 'Quarterly business review với khách hàng VIP. Feedback tích cực.', NOW() - INTERVAL '95 days', NOW() - INTERVAL '95 days', NOW(), NULL),
(3, 13, 'EMAIL', 'Gửi báo cáo performance và ROI. Khách hàng hài lòng với kết quả.', NOW() - INTERVAL '90 days', NOW() - INTERVAL '90 days', NOW(), NULL),
(4, 5, 'CALL', 'Hỗ trợ onboarding cho trial user. Hướng dẫn setup ban đầu.', NOW() - INTERVAL '88 days', NOW() - INTERVAL '88 days', NOW(), NULL),
(5, 6, 'EMAIL', 'Gửi contract renewal notice. Còn 2 tháng hết hạn.', NOW() - INTERVAL '80 days', NOW() - INTERVAL '80 days', NOW(), NULL),
(6, 6, 'CALL', 'Technical support call về integration issues. Đã giải quyết.', NOW() - INTERVAL '75 days', NOW() - INTERVAL '75 days', NOW(), NULL),
(7, 12, 'MEETING', 'First meeting với referral customer. Introduction và needs assessment.', NOW() - INTERVAL '73 days', NOW() - INTERVAL '73 days', NOW(), NULL),
(8, 4, 'MEETING', 'Enterprise contract negotiation. Đang thảo luận terms & conditions.', NOW() - INTERVAL '68 days', NOW() - INTERVAL '68 days', NOW(), NULL),
(9, 6, 'CALL', 'Retail customer inquiry về bulk pricing. Gửi special quote.', NOW() - INTERVAL '63 days', NOW() - INTERVAL '63 days', NOW(), NULL),
(10, 13, 'MEETING', 'Contract negotiation meeting. Discussing payment terms.', NOW() - INTERVAL '58 days', NOW() - INTERVAL '58 days', NOW(), NULL),
(11, 5, 'EMAIL', 'Send case studies về manufacturing industry. Building trust.', NOW() - INTERVAL '53 days', NOW() - INTERVAL '53 days', NOW(), NULL),
(12, 13, 'CALL', 'Education sector special pricing discussion. Khách hàng đồng ý.', NOW() - INTERVAL '48 days', NOW() - INTERVAL '48 days', NOW(), NULL),
(13, 15, 'MEETING', 'Healthcare compliance và security meeting. Address concerns.', NOW() - INTERVAL '43 days', NOW() - INTERVAL '43 days', NOW(), NULL),
(14, 4, 'EMAIL', 'Finance sector compliance documentation sent. Awaiting approval.', NOW() - INTERVAL '38 days', NOW() - INTERVAL '38 days', NOW(), NULL),
(15, 5, 'CALL', 'Travel industry seasonal planning call. Discuss peak season support.', NOW() - INTERVAL '33 days', NOW() - INTERVAL '33 days', NOW(), NULL),
(16, 14, 'MEETING', 'F&B multi-branch deployment planning. Technical requirements discussion.', NOW() - INTERVAL '28 days', NOW() - INTERVAL '28 days', NOW(), NULL);

-- =============================================================================
-- 7. NOTES TABLE (20 records)
-- =============================================================================
INSERT INTO notes (customer_id, author_id, content, created_at, updated_at, deleted_at) VALUES
(1, 4, 'Khách hàng có budget khoảng $50,000/năm. Decision maker là CEO.', NOW() - INTERVAL '117 days', NOW(), NULL),
(1, 12, 'Follow-up vào tuần tới. Khách hàng đang so sánh với 2 competitor khác.', NOW() - INTERVAL '109 days', NOW(), NULL),
(2, 4, 'User hiện tại: 50 seats. Dự kiến mở rộng lên 100 seats trong Q4.', NOW() - INTERVAL '104 days', NOW(), NULL),
(3, 5, 'VIP customer - always prioritize support tickets. Response time < 1 hour.', NOW() - INTERVAL '94 days', NOW(), NULL),
(3, 13, 'Renewal date: 15/06/2025. Start discussion 2 months before.', NOW() - INTERVAL '89 days', NOW(), NULL),
(4, 5, 'Trial ends on 20/03/2025. Schedule conversion call 1 week before.', NOW() - INTERVAL '87 days', NOW(), NULL),
(5, 6, 'Contract signed 1 year. Very happy with support quality.', NOW() - INTERVAL '84 days', NOW(), NULL),
(6, 6, 'Technical issues với API integration. Cần follow-up với tech team.', NOW() - INTERVAL '74 days', NOW(), NULL),
(7, 12, 'Referred by customer #3. Quan hệ tốt, có thể close deal nhanh.', NOW() - INTERVAL '72 days', NOW(), NULL),
(8, 4, 'Enterprise deal worth $150,000. High priority. CEO approval needed.', NOW() - INTERVAL '67 days', NOW(), NULL),
(9, 6, 'Retail customer với 20 locations. Cần bulk pricing và training support.', NOW() - INTERVAL '62 days', NOW(), NULL),
(10, 13, 'Logistics company - focus on mobile app features. Android priority.', NOW() - INTERVAL '57 days', NOW(), NULL),
(11, 5, 'Manufacturing - cần custom reporting features. Quote đã gửi.', NOW() - INTERVAL '52 days', NOW(), NULL),
(12, 13, 'Education sector - có thể giảm 20% cho nonprofit. Manager đã approve.', NOW() - INTERVAL '47 days', NOW(), NULL),
(13, 15, 'Healthcare - HIPAA compliance là must-have. Security audit scheduled.', NOW() - INTERVAL '42 days', NOW(), NULL),
(14, 4, 'Finance sector - need SOC2 compliance certificate. Preparing documentation.', NOW() - INTERVAL '37 days', NOW(), NULL),
(15, 5, 'Travel - seasonal business. High traffic Q2 & Q3. Infrastructure planning needed.', NOW() - INTERVAL '32 days', NOW(), NULL),
(16, 14, 'F&B - 15 branches, expanding to 30 in 2025. Scalability is key concern.', NOW() - INTERVAL '27 days', NOW(), NULL),
(17, 12, 'Real estate - focus on CRM for property management. Custom fields needed.', NOW() - INTERVAL '23 days', NOW(), NULL),
(18, 17, 'Media - content collaboration features important. Integration with CMS needed.', NOW() - INTERVAL '18 days', NOW(), NULL);

-- =============================================================================
-- 8. COMMENTS TABLE (20 records)
-- =============================================================================
INSERT INTO comments (entity_type, entity_id, author_id, content, parent_id, is_edited, created_at, updated_at, deleted_at) VALUES
('CUSTOMER', 1, 4, 'Đã gửi proposal. Waiting for response from CEO.', NULL, false, NOW() - INTERVAL '116 days', NOW(), NULL),
('CUSTOMER', 1, 12, 'CEO vừa gọi lại, cần clarify về pricing model.', NULL, false, NOW() - INTERVAL '111 days', NOW(), NULL),
('CUSTOMER', 1, 2, '@editor1 Please schedule follow-up meeting ASAP.', NULL, false, NOW() - INTERVAL '108 days', NOW(), NULL),
('CUSTOMER', 2, 4, 'Customer đã đồng ý upgrade lên Premium package.', NULL, false, NOW() - INTERVAL '102 days', NOW(), NULL),
('CUSTOMER', 3, 5, 'QBR went great! Customer very satisfied with our service.', NULL, false, NOW() - INTERVAL '93 days', NOW(), NULL),
('CUSTOMER', 3, 13, 'NPS score: 9/10. Excellent feedback!', NULL, false, NOW() - INTERVAL '88 days', NOW(), NULL),
('INTERACTION', 1, 12, 'Great meeting! Customer impressed with Enterprise features.', NULL, false, NOW() - INTERVAL '117 days', NOW(), NULL),
('INTERACTION', 2, 4, 'Quotation sent via email. Follow-up in 3 days.', NULL, false, NOW() - INTERVAL '114 days', NOW(), NULL),
('INTERACTION', 3, 12, 'Customer needs internal approval. Will update next week.', NULL, false, NOW() - INTERVAL '109 days', NOW(), NULL),
('CUSTOMER', 8, 4, 'Enterprise deal in final stage. Legal review in progress.', NULL, false, NOW() - INTERVAL '66 days', NOW(), NULL),
('CUSTOMER', 8, 2, 'Priority customer - expedite contract processing.', NULL, false, NOW() - INTERVAL '65 days', NOW(), NULL),
('INTERACTION', 6, 5, 'VIP customer QBR completed. Discussed roadmap for 2025.', NULL, false, NOW() - INTERVAL '94 days', NOW(), NULL),
('INTERACTION', 10, 6, 'Technical issue resolved. Customer happy with quick response.', NULL, false, NOW() - INTERVAL '74 days', NOW(), NULL),
('CUSTOMER', 13, 15, 'Healthcare compliance docs ready. Sending to customer tomorrow.', NULL, false, NOW() - INTERVAL '41 days', NOW(), NULL),
('CUSTOMER', 14, 4, 'Finance customer - schedule security audit next month.', NULL, false, NOW() - INTERVAL '36 days', NOW(), NULL),
('INTERACTION', 16, 13, 'Education pricing approved. Contract draft ready.', NULL, false, NOW() - INTERVAL '47 days', NOW(), NULL),
('CUSTOMER', 17, 12, 'Real estate customer needs demo of property management module.', NULL, false, NOW() - INTERVAL '22 days', NOW(), NULL),
('CUSTOMER', 18, 17, 'Media customer - integration with WordPress confirmed possible.', NULL, false, NOW() - INTERVAL '17 days', NOW(), NULL),
('CUSTOMER', 20, 13, 'E-commerce customer - discussing API rate limits.', NULL, false, NOW() - INTERVAL '8 days', NOW(), NULL),
('INTERACTION', 20, 14, 'F&B deployment planning meeting scheduled for next week.', NULL, false, NOW() - INTERVAL '27 days', NOW(), NULL);

-- =============================================================================
-- 9. ACTIVITIES TABLE (20 records)
-- =============================================================================
INSERT INTO activities (user_id, action, entity_type, entity_id, details, ip_address, user_agent, timestamp) VALUES
(4, 'CREATE', 'CUSTOMER', 1, '{"customer_name": "Nguyễn Văn A", "company": "TechCorp Vietnam"}', '192.168.1.100', 'Mozilla/5.0', NOW() - INTERVAL '120 days'),
(4, 'UPDATE', 'CUSTOMER', 1, '{"field": "notes", "old_value": "", "new_value": "Khách hàng tiềm năng..."}', '192.168.1.100', 'Mozilla/5.0', NOW() - INTERVAL '117 days'),
(4, 'CREATE', 'INTERACTION', 1, '{"type": "MEETING", "customer_id": 1}', '192.168.1.100', 'Mozilla/5.0', NOW() - INTERVAL '118 days'),
(12, 'CREATE', 'INTERACTION', 3, '{"type": "CALL", "customer_id": 1}', '192.168.1.101', 'Mozilla/5.0', NOW() - INTERVAL '110 days'),
(5, 'CREATE', 'CUSTOMER', 3, '{"customer_name": "Lê Văn C", "company": "SmartTech JSC"}', '192.168.1.102', 'Mozilla/5.0', NOW() - INTERVAL '100 days'),
(5, 'CREATE', 'INTERACTION', 6, '{"type": "MEETING", "customer_id": 3}', '192.168.1.102', 'Mozilla/5.0', NOW() - INTERVAL '95 days'),
(13, 'ASSIGN', 'CUSTOMER', 3, '{"assigned_user": "sale2@crm.com", "type": "COLLABORATOR"}', '192.168.1.103', 'Mozilla/5.0', NOW() - INTERVAL '50 days'),
(6, 'CREATE', 'CUSTOMER', 5, '{"customer_name": "Hoàng Văn E", "company": "FutureSoft Co."}', '192.168.1.104', 'Mozilla/5.0', NOW() - INTERVAL '85 days'),
(6, 'UPDATE', 'CUSTOMER', 6, '{"field": "status", "old_value": "trial", "new_value": "active"}', '192.168.1.104', 'Mozilla/5.0', NOW() - INTERVAL '77 days'),
(4, 'CREATE', 'NOTE', 1, '{"customer_id": 1, "note_length": 55}', '192.168.1.100', 'Mozilla/5.0', NOW() - INTERVAL '117 days'),
(12, 'CREATE', 'NOTE', 2, '{"customer_id": 1, "note_length": 68}', '192.168.1.101', 'Mozilla/5.0', NOW() - INTERVAL '109 days'),
(4, 'CREATE', 'COMMENT', 1, '{"entity_type": "CUSTOMER", "entity_id": 1}', '192.168.1.100', 'Mozilla/5.0', NOW() - INTERVAL '116 days'),
(2, 'ASSIGN', 'CUSTOMER', 1, '{"assigned_user": "sale1@crm.com", "type": "COLLABORATOR"}', '192.168.1.99', 'Mozilla/5.0', NOW() - INTERVAL '60 days'),
(13, 'CREATE', 'CUSTOMER', 10, '{"customer_name": "Dương Thị K", "company": "Vietnam Logistics"}', '192.168.1.103', 'Mozilla/5.0', NOW() - INTERVAL '60 days'),
(15, 'CREATE', 'CUSTOMER', 13, '{"customer_name": "Phan Văn N", "company": "Healthcare Solutions"}', '192.168.1.105', 'Mozilla/5.0', NOW() - INTERVAL '45 days'),
(17, 'CREATE', 'CUSTOMER', 18, '{"customer_name": "Đinh Thị S", "company": "Media Network"}', '192.168.1.106', 'Mozilla/5.0', NOW() - INTERVAL '20 days'),
(4, 'UPDATE', 'CUSTOMER', 8, '{"field": "notes", "value": "Enterprise deal worth $150,000"}', '192.168.1.100', 'Mozilla/5.0', NOW() - INTERVAL '67 days'),
(5, 'DELETE', 'INTERACTION', 5, '{"reason": "Duplicate entry", "customer_id": 2}', '192.168.1.102', 'Mozilla/5.0', NOW() - INTERVAL '98 days'),
(13, 'CREATE', 'INTERACTION', 14, '{"type": "MEETING", "customer_id": 10}', '192.168.1.103', 'Mozilla/5.0', NOW() - INTERVAL '58 days'),
(14, 'CREATE', 'INTERACTION', 20, '{"type": "MEETING", "customer_id": 16}', '192.168.1.107', 'Mozilla/5.0', NOW() - INTERVAL '28 days');

-- =============================================================================
-- VERIFICATION QUERIES
-- =============================================================================
SELECT 'Data inserted successfully!' as status;
SELECT 'Users:', COUNT(*) FROM users;
SELECT 'Teams:', COUNT(*) FROM teams;
SELECT 'Team Members:', COUNT(*) FROM team_members;
SELECT 'Customers:', COUNT(*) FROM customers;
SELECT 'Customer Assignments:', COUNT(*) FROM customer_assignments;
SELECT 'Interactions:', COUNT(*) FROM interactions;
SELECT 'Notes:', COUNT(*) FROM notes;
SELECT 'Comments:', COUNT(*) FROM comments;
SELECT 'Activities:', COUNT(*) FROM activities;