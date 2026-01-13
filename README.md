# School Service Management System

A comprehensive platform for managing school transportation operations, focusing on student safety, transparency, and operational efficiency.

## 🚀 Tech Stack
- **Backend**: Java 17, Spring Boot 3.x, PostgreSQL
- **Security**: JWT-based authentication with role-based access control (RBAC)
- **Frontend**: React 19 + Next.js 15
- **Styling**: Bootstrap

## 👥 User Roles
- **Admin**: System-wide management
- **School Admin**: School-specific operations
- **Driver**: Vehicle operation
- **Hostess**: Student safety and attendance
- **Parent**: Student service and payment information

## 🧩 Core Modules
- Authentication & User Management
- Student Management
- Route & Stop Management
- Vehicle Crew Management (Driver & Hostess)
- Payment System

## 📜 Business Rules (Vehicle Crew)
- Each vehicle must have at least one active driver
- Each vehicle can have at most one active hostess
- Driver/Hostess can only be assigned to one vehicle at a time
- Inactive personnel cannot be assigned

## 🔗 API Endpoints
- `POST /api/hostesses`
- `PUT /api/vehicles/{id}/assign-hostess`
- `POST /api/drivers`
- `PUT /api/vehicles/{id}/assign-driver`

## 🔒 Security
- JWT-based authentication
- Role-based authorization (RBAC)

## 🗺️ Roadmap
- Hostess mobile interface
- Student attendance system
- Notification infrastructure (SMS / Push)
- Reporting dashboards

## ⚙️ Installation

### Backend
```bash
# Clone repository
git clone https://github.com/your-username/school-service.git
cd school-service/backend

# Build & run
./mvnw spring-boot:run
