# AirSpace Live Sim - Frontend

A real-time 3D flight tracking visualization built with React, TypeScript, and CesiumJS.

## Features

- **3D Earth Globe**: Interactive globe with realistic rendering
- **Day/Night Lighting**: Dynamic lighting based on sun position
- **Real-time Flight Data**: Fetches and displays live flight positions from the backend
- **Flight Information Panel**: Shows active flights with details
- **Smooth Updates**: Automatic data refresh every 5 seconds
- **Modern UI**: Clean, dark-themed interface with glassmorphism effects

## Architecture

```
webapp/
├── src/
│   ├── api/              # API services for backend communication
│   │   └── flightApi.ts  # Flight data fetching
│   ├── globe/            # Cesium globe components
│   │   └── CesiumGlobe.tsx  # Main globe with airplane rendering
│   ├── components/       # UI components
│   │   ├── FlightPanel.tsx   # Flight list panel
│   │   ├── FlightPanel.css
│   │   ├── Header.tsx        # App header
│   │   └── Header.css
│   ├── types/            # TypeScript type definitions
│   │   └── flight.ts     # Flight data models
│   ├── App.tsx           # Main application component
│   ├── App.css
│   └── main.tsx          # Application entry point
├── package.json
├── vite.config.ts        # Vite configuration with Cesium plugin
└── tsconfig.json
```

## Prerequisites

- Node.js 18+ and npm
- Spring Boot backend running on `http://localhost:8080`

## Installation

1. Install dependencies:
```bash
cd webapp
npm install
```

## Running Locally

1. Make sure the Spring Boot backend is running on port 8080:
```bash
cd ../flight-tracker
# Start your Spring Boot application
```

2. Start the development server:
```bash
npm run dev
```

3. Open your browser and navigate to:
```
http://localhost:3000
```

## Available Scripts

- `npm run dev` - Start development server on port 3000
- `npm run build` - Build for production
- `npm run preview` - Preview production build
- `npm run lint` - Run ESLint

## Configuration

### API Endpoint

The API endpoint is configured in `src/api/flightApi.ts`:

```typescript
const API_BASE_URL = 'http://localhost:8080/api';
```

To change the backend URL, modify this constant.

### Cesium Access Token

The Cesium Ion access token is set in `src/globe/CesiumGlobe.tsx`. The current token is for development purposes. For production, obtain your own token from [Cesium Ion](https://cesium.com/ion/).

### Update Interval

Flight data is fetched every 5 seconds by default. To change this, modify the interval in `src/App.tsx`:

```typescript
const interval = setInterval(fetchFlights, 5000); // 5000ms = 5 seconds
```

## Technologies Used

- **React 18** - UI framework
- **TypeScript** - Type-safe JavaScript
- **Vite** - Build tool and dev server
- **CesiumJS** - 3D globe and geospatial visualization
- **CSS3** - Styling with modern features (backdrop-filter, gradients)

## Key Components

### CesiumGlobe

The main globe component that:
- Initializes the Cesium viewer
- Enables day/night lighting
- Renders airplanes as entities with labels
- Updates positions smoothly in real-time
- Shows flight information on click

### FlightPanel

A side panel that:
- Lists all active flights
- Shows flight routes (departure → destination)
- Displays altitude and speed
- Color-codes flight status
- Supports flight selection (ready for future features)

### FlightApi

Service layer that handles:
- Fetching all flights
- Fetching individual flight by ID
- Searching flights (ready for future features)
- Error handling

## Future Enhancements

- Flight path history visualization
- Search and filter functionality
- Airport markers on the globe
- Flight detail modal
- WebSocket support for real-time updates
- 3D airplane models instead of simple points
- Time controls for historical playback
- Performance optimizations for thousands of flights

## Troubleshooting

### Backend Connection Error

If you see "Failed to fetch flight data" error:
1. Verify the Spring Boot backend is running
2. Check it's accessible at `http://localhost:8080/api/flights`
3. Check browser console for CORS errors
4. Ensure backend has CORS properly configured

### Cesium Assets Not Loading

If the globe doesn't render:
1. Check browser console for Cesium errors
2. Verify internet connection (Cesium loads assets from CDN)
3. Try using a different Cesium Ion token

### Performance Issues

If the app is slow with many flights:
1. Reduce the update interval
2. Limit the number of flights rendered
3. Disable labels for distant flights
4. Consider implementing clustering

## License

Part of the AirSpace Live Sim project.
