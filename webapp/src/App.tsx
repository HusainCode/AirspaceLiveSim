import { useState, useEffect } from 'react';
import { CesiumGlobe } from './globe/CesiumGlobe';
import { FlightPanel } from './components/FlightPanel';
import { Header } from './components/Header';
import { FlightApi } from './api/flightApi';
import type { Flight } from './types/flight';
import './App.css';

function App() {
  const [flights, setFlights] = useState<Flight[]>([]);
  const [lastUpdate, setLastUpdate] = useState<Date | undefined>();
  const [error, setError] = useState<string | null>(null);

  const fetchFlights = async () => {
    try {
      const data = await FlightApi.getAllFlights();
      setFlights(data);
      setLastUpdate(new Date());
      setError(null);
    } catch (err) {
      console.error('Failed to fetch flights:', err);
      setError('Failed to fetch flight data. Make sure the backend is running on http://localhost:8080');
    }
  };

  useEffect(() => {
    fetchFlights();

    const interval = setInterval(fetchFlights, 5000);

    return () => clearInterval(interval);
  }, []);

  return (
    <div className="app">
      <Header lastUpdate={lastUpdate} />
      <CesiumGlobe flights={flights} />
      <FlightPanel flights={flights} />

      {error && (
        <div className="error-banner">
          {error}
        </div>
      )}
    </div>
  );
}

export default App;
