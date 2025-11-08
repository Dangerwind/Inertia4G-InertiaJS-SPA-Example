build-frontend:
	cd frontend && npm install && npm run build

dev-frontend:
	cd frontend && npm run dev

run-backend:
	cd backend && ./gradlew clean build && ./gradlew run