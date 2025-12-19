# Quick Start Guide

## Installation & Running

### 1. Install Dependencies
```bash
cd webapp
npm install
```

### 2. Start Backend
Ensure your Spring Boot backend is running on port 8080:
```bash
cd ../flight-tracker
# Start your Spring Boot application
./mvnw spring-boot:run
# or
java -jar target/flight-tracker.jar
```

### 3. Start Frontend
```bash
cd webapp
npm run dev
```

### 4. Open Browser
Navigate to: http://localhost:3000

## Folder Structure

```
webapp/
├── src/
│   ├── api/
│   │   └── flightApi.ts          # API client for backend
│   ├── globe/
│   │   └── CesiumGlobe.tsx       # 3D Earth globe with airplanes
│   ├── components/
│   │   ├── FlightPanel.tsx       # Flight list sidebar
│   │   ├── FlightPanel.css
│   │   ├── Header.tsx            # App header
│   │   └── Header.css
│   ├── types/
│   │   └── flight.ts             # TypeScript interfaces
│   ├── App.tsx                   # Main app component
│   ├── App.css
│   └── main.tsx                  # Entry point
├── package.json
├── vite.config.ts
├── tsconfig.json
└── README.md
```

## Key Features Implemented

✅ 3D Earth globe with CesiumJS
✅ Day/night lighting based on sun position
✅ Real-time flight data fetching (5-second interval)
✅ Airplane markers with labels
✅ Flight information panel
✅ Click on flights for details
✅ Smooth position updates
✅ Modern glassmorphism UI
✅ Error handling with user feedback

## API Endpoints Used

- `GET http://localhost:8080/api/flights` - Fetch all flights

## Technologies

- **React 18** - UI framework
- **TypeScript** - Type safety
- **Vite** - Build tool (fast HMR)
- **CesiumJS** - 3D geospatial visualization
- **CSS3** - Modern styling

## Development

- Dev server runs on port 3000
- Auto-refresh every 5 seconds
- Hot module replacement (HMR) enabled
- TypeScript strict mode

## Build for Production

```bash
npm run build
npm run preview
```

Builds to `dist/` folder.
