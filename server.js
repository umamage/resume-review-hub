import express from 'express';
import path from 'path';
import { fileURLToPath } from 'url';
import fs from 'fs';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const app = express();
const PORT = process.env.PORT || 3000;

// Serve static files from dist directory
const distPath = path.join(__dirname, 'dist');

// Check if dist directory exists
if (!fs.existsSync(distPath)) {
  console.error('Error: dist directory not found. Please run "npm run build" first.');
  process.exit(1);
}

app.use(express.static(distPath, {
  maxAge: '1h',
  etag: false,
}));

// SPA fallback - serve index.html for all non-static routes
app.get('*', (req, res) => {
  res.sendFile(path.join(distPath, 'index.html'), (err) => {
    if (err) {
      console.error('Error serving index.html:', err);
      res.status(500).send('Server error');
    }
  });
});

app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
  console.log(`Serving files from ${distPath}`);
});
