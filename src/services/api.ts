// API Service for Resume Review Hub
// This service handles all communication with the backend

const getApiBaseUrl = (): string => {
  // Try to get from environment variable
  const envUrl = import.meta.env.VITE_API_BASE_URL;
  if (envUrl) {
    return envUrl;
  }

  // Fallback based on environment
  if (typeof window !== 'undefined' && window.location.hostname === 'localhost') {
    return 'http://localhost:8080/api';
  }

  // For production on Render, construct from current domain
  if (typeof window !== 'undefined') {
    const hostname = window.location.hostname;
    if (hostname.includes('onrender.com')) {
      // Extract the base name and replace frontend with backend
      const backendUrl = hostname.replace('frontend', 'backend').replace('resume-review-hub', 'resume-review-backend');
      return `https://${backendUrl}/api`;
    }
  }

  // Default fallback
  return '/api';
};

export const apiBaseUrl = getApiBaseUrl();

// Resume API endpoints
export const resumeApi = {
  // Upload a resume and get analysis
  uploadResume: async (file: File): Promise<{ score: number; suggestions: string[] }> => {
    const formData = new FormData();
    formData.append('file', file);

    const response = await fetch(`${apiBaseUrl}/resumes/upload`, {
      method: 'POST',
      body: formData,
      headers: {
        // Don't set Content-Type, browser will set it with boundary
      },
    });

    if (!response.ok) {
      throw new Error(`Failed to upload resume: ${response.statusText}`);
    }

    return response.json();
  },

  // Get all resumes
  getResumes: async (): Promise<any[]> => {
    const response = await fetch(`${apiBaseUrl}/resumes`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      throw new Error(`Failed to fetch resumes: ${response.statusText}`);
    }

    return response.json();
  },

  // Get resume by ID
  getResumeById: async (id: string): Promise<any> => {
    const response = await fetch(`${apiBaseUrl}/resumes/${id}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      throw new Error(`Failed to fetch resume: ${response.statusText}`);
    }

    return response.json();
  },

  // Update resume score
  updateResumeScore: async (id: string, score: number): Promise<any> => {
    const response = await fetch(`${apiBaseUrl}/resumes/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ score }),
    });

    if (!response.ok) {
      throw new Error(`Failed to update resume: ${response.statusText}`);
    }

    return response.json();
  },

  // Delete resume
  deleteResume: async (id: string): Promise<void> => {
    const response = await fetch(`${apiBaseUrl}/resumes/${id}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      throw new Error(`Failed to delete resume: ${response.statusText}`);
    }
  },
};

// Job API endpoints
export const jobApi = {
  // Get job suggestions
  getJobSuggestions: async (score: number): Promise<any[]> => {
    const response = await fetch(`${apiBaseUrl}/jobs/suggestions?score=${score}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      throw new Error(`Failed to fetch job suggestions: ${response.statusText}`);
    }

    return response.json();
  },

  // Get all jobs
  getJobs: async (): Promise<any[]> => {
    const response = await fetch(`${apiBaseUrl}/jobs`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      throw new Error(`Failed to fetch jobs: ${response.statusText}`);
    }

    return response.json();
  },

  // Apply for a job
  applyForJob: async (jobId: string, resumeId: string): Promise<any> => {
    const response = await fetch(`${apiBaseUrl}/jobs/${jobId}/apply`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ resumeId }),
    });

    if (!response.ok) {
      throw new Error(`Failed to apply for job: ${response.statusText}`);
    }

    return response.json();
  },
};

// Health check
export const checkBackendHealth = async (): Promise<boolean> => {
  try {
    const response = await fetch(`${apiBaseUrl}/health`, {
      method: 'GET',
    });
    return response.ok;
  } catch (error) {
    console.error('Backend health check failed:', error);
    return false;
  }
};

export default {
  apiBaseUrl,
  resumeApi,
  jobApi,
  checkBackendHealth,
};
