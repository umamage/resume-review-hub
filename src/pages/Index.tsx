import { useState } from "react";
import Header from "@/components/Header";
import ResumeUpload from "@/components/ResumeUpload";
import ScoreDisplay from "@/components/ScoreDisplay";
import JobSuggestions from "@/components/JobSuggestions";

const Index = () => {
  const [activeTab, setActiveTab] = useState("upload");
  const [hasUploadedResume, setHasUploadedResume] = useState(false);
  const [resumeScore] = useState(78); // This would come from the Java backend

  const handleUploadComplete = (file: File) => {
    console.log("Resume uploaded:", file.name);
    setHasUploadedResume(true);
    // Here you would send the file to your Java backend
    // and receive the score back
    setActiveTab("review");
  };

  return (
    <div className="min-h-screen gradient-hero">
      <Header activeTab={activeTab} onTabChange={setActiveTab} />
      
      <main>
        {activeTab === "upload" && (
          <ResumeUpload onUploadComplete={handleUploadComplete} />
        )}
        
        {activeTab === "review" && (
          <ScoreDisplay score={resumeScore} isVisible={true} />
        )}
        
        {activeTab === "jobs" && (
          <JobSuggestions isVisible={true} />
        )}
      </main>

      {/* Footer */}
      <footer className="border-t border-border bg-card py-8 mt-16">
        <div className="container text-center text-sm text-muted-foreground">
          <p>© 2024 ResumeAI. Powered by advanced AI analysis.</p>
        </div>
      </footer>
    </div>
  );
};

export default Index;
