import { useState, useCallback } from "react";
import { Upload, FileText, CheckCircle, Loader2 } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";

interface ResumeUploadProps {
  onUploadComplete: (file: File) => void;
}

const ResumeUpload = ({ onUploadComplete }: ResumeUploadProps) => {
  const [isDragging, setIsDragging] = useState(false);
  const [uploadedFile, setUploadedFile] = useState<File | null>(null);
  const [isUploading, setIsUploading] = useState(false);

  const handleDragOver = useCallback((e: React.DragEvent) => {
    e.preventDefault();
    setIsDragging(true);
  }, []);

  const handleDragLeave = useCallback((e: React.DragEvent) => {
    e.preventDefault();
    setIsDragging(false);
  }, []);

  const handleDrop = useCallback((e: React.DragEvent) => {
    e.preventDefault();
    setIsDragging(false);
    const file = e.dataTransfer.files[0];
    if (file && (file.type === "application/pdf" || file.type.includes("document"))) {
      processFile(file);
    }
  }, []);

  const handleFileInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      processFile(file);
    }
  };

  const processFile = async (file: File) => {
    setIsUploading(true);
    // Simulate upload delay for demo
    await new Promise(resolve => setTimeout(resolve, 1500));
    setUploadedFile(file);
    setIsUploading(false);
    onUploadComplete(file);
  };

  return (
    <section className="py-16 md:py-24">
      <div className="container max-w-4xl">
        <div className="text-center mb-12 animate-fade-in-up">
          <h1 className="font-display text-4xl md:text-5xl font-bold text-foreground mb-4">
            Get Your Resume <span className="text-gradient">Reviewed</span>
          </h1>
          <p className="text-lg text-muted-foreground max-w-2xl mx-auto">
            Upload your resume and get instant AI-powered feedback, score analysis, 
            and personalized job recommendations.
          </p>
        </div>

        <Card
          onDragOver={handleDragOver}
          onDragLeave={handleDragLeave}
          onDrop={handleDrop}
          className={`
            relative overflow-hidden border-2 border-dashed transition-all duration-300
            ${isDragging 
              ? "border-primary bg-primary/5 scale-[1.02]" 
              : uploadedFile 
                ? "border-success bg-success/5" 
                : "border-border hover:border-primary/50 hover:bg-secondary/50"
            }
          `}
        >
          <div className="p-12 md:p-16 text-center">
            {isUploading ? (
              <div className="animate-scale-in">
                <div className="relative inline-flex">
                  <div className="absolute inset-0 rounded-full gradient-primary opacity-20 animate-pulse-ring" />
                  <div className="relative flex h-20 w-20 items-center justify-center rounded-full bg-primary/10">
                    <Loader2 className="h-10 w-10 text-primary animate-spin" />
                  </div>
                </div>
                <p className="mt-6 text-lg font-medium text-foreground">Analyzing your resume...</p>
                <p className="mt-2 text-muted-foreground">This may take a few moments</p>
              </div>
            ) : uploadedFile ? (
              <div className="animate-scale-in">
                <div className="relative inline-flex">
                  <div className="absolute inset-0 rounded-full bg-success/20 animate-pulse-ring" />
                  <div className="relative flex h-20 w-20 items-center justify-center rounded-full bg-success/10">
                    <CheckCircle className="h-10 w-10 text-success" />
                  </div>
                </div>
                <p className="mt-6 text-lg font-medium text-foreground">Resume uploaded successfully!</p>
                <p className="mt-2 text-muted-foreground">{uploadedFile.name}</p>
                <Button 
                  variant="outline" 
                  className="mt-6"
                  onClick={() => setUploadedFile(null)}
                >
                  Upload Different Resume
                </Button>
              </div>
            ) : (
              <>
                <div className="relative inline-flex mb-6">
                  <div className="flex h-20 w-20 items-center justify-center rounded-full bg-primary/10">
                    <Upload className="h-10 w-10 text-primary" />
                  </div>
                </div>
                <p className="text-lg font-medium text-foreground mb-2">
                  Drag and drop your resume here
                </p>
                <p className="text-muted-foreground mb-6">
                  Supports PDF, DOC, DOCX (Max 10MB)
                </p>
                <label>
                  <input
                    type="file"
                    accept=".pdf,.doc,.docx"
                    onChange={handleFileInput}
                    className="hidden"
                  />
                  <Button variant="gradient" size="lg" asChild>
                    <span className="cursor-pointer">
                      <FileText className="h-5 w-5 mr-2" />
                      Browse Files
                    </span>
                  </Button>
                </label>
              </>
            )}
          </div>
        </Card>
      </div>
    </section>
  );
};

export default ResumeUpload;
