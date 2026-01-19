import express from 'express';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const app = express();
const PORT = process.env.PORT || 3000;

// Serve static files and the single HTML file
app.use(express.static(__dirname));

// Serve the main HTML file for all routes (SPA style)
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'index-lite.html'));
});

app.get('*', (req, res) => {
    res.sendFile(path.join(__dirname, 'index-lite.html'));
});

app.listen(PORT, () => {
    console.log(`Resume Review Frontend running on port ${PORT}`);
});
