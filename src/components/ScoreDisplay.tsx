import { Card } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { CheckCircle, AlertCircle, TrendingUp, FileText, Briefcase, GraduationCap } from "lucide-react";

interface ScoreDisplayProps {
  score: number;
  isVisible: boolean;
}

const ScoreDisplay = ({ score, isVisible }: ScoreDisplayProps) => {
  const circumference = 2 * Math.PI * 45;
  const strokeDashoffset = circumference - (score / 100) * circumference;

  const getScoreColor = () => {
    if (score >= 80) return "text-success";
    if (score >= 60) return "text-warning";
    return "text-destructive";
  };

  const getScoreLabel = () => {
    if (score >= 80) return { text: "Excellent", color: "bg-success" };
    if (score >= 60) return { text: "Good", color: "bg-warning" };
    return { text: "Needs Work", color: "bg-destructive" };
  };

  const improvements = [
    { icon: CheckCircle, text: "Strong work experience section", positive: true },
    { icon: CheckCircle, text: "Good keyword optimization", positive: true },
    { icon: AlertCircle, text: "Add more quantifiable achievements", positive: false },
    { icon: AlertCircle, text: "Include relevant certifications", positive: false },
  ];

  const categories = [
    { icon: FileText, label: "Format & Layout", score: 85, color: "text-primary" },
    { icon: Briefcase, label: "Work Experience", score: 78, color: "text-accent" },
    { icon: GraduationCap, label: "Skills & Education", score: 72, color: "text-success" },
    { icon: TrendingUp, label: "ATS Compatibility", score: 88, color: "text-warning" },
  ];

  if (!isVisible) return null;

  return (
    <section className="py-16 md:py-24 gradient-hero">
      <div className="container max-w-6xl">
        <div className="text-center mb-12 animate-fade-in-up">
          <h2 className="font-display text-3xl md:text-4xl font-bold text-foreground mb-4">
            Your Resume <span className="text-gradient">Score</span>
          </h2>
          <p className="text-lg text-muted-foreground">
            Here's how your resume stacks up against industry standards
          </p>
        </div>

        <div className="grid md:grid-cols-2 gap-8">
          {/* Main Score Card */}
          <Card className="p-8 shadow-card animate-fade-in-up">
            <div className="flex flex-col items-center">
              <div className="relative">
                <svg className="w-48 h-48 transform -rotate-90">
                  <circle
                    cx="96"
                    cy="96"
                    r="45"
                    stroke="currentColor"
                    strokeWidth="8"
                    fill="none"
                    className="text-secondary"
                  />
                  <circle
                    cx="96"
                    cy="96"
                    r="45"
                    stroke="url(#scoreGradient)"
                    strokeWidth="8"
                    fill="none"
                    strokeLinecap="round"
                    strokeDasharray={circumference}
                    strokeDashoffset={strokeDashoffset}
                    className="animate-progress-fill"
                    style={{ 
                      animationDelay: "0.3s",
                      strokeDashoffset: strokeDashoffset 
                    }}
                  />
                  <defs>
                    <linearGradient id="scoreGradient" x1="0%" y1="0%" x2="100%" y2="100%">
                      <stop offset="0%" stopColor="hsl(210, 100%, 45%)" />
                      <stop offset="100%" stopColor="hsl(172, 66%, 40%)" />
                    </linearGradient>
                  </defs>
                </svg>
                <div className="absolute inset-0 flex flex-col items-center justify-center">
                  <span className={`font-display text-5xl font-bold ${getScoreColor()}`}>
                    {score}
                  </span>
                  <span className="text-muted-foreground text-sm">out of 100</span>
                </div>
              </div>
              <Badge className={`mt-6 ${getScoreLabel().color} text-primary-foreground`}>
                {getScoreLabel().text}
              </Badge>
            </div>

            {/* Category Breakdown */}
            <div className="mt-8 space-y-4">
              {categories.map((category, index) => (
                <div key={index} className="flex items-center gap-4">
                  <div className={`p-2 rounded-lg bg-secondary ${category.color}`}>
                    <category.icon className="h-4 w-4" />
                  </div>
                  <div className="flex-1">
                    <div className="flex justify-between mb-1">
                      <span className="text-sm font-medium text-foreground">{category.label}</span>
                      <span className="text-sm text-muted-foreground">{category.score}%</span>
                    </div>
                    <div className="h-2 bg-secondary rounded-full overflow-hidden">
                      <div 
                        className="h-full gradient-primary rounded-full transition-all duration-1000"
                        style={{ width: `${category.score}%` }}
                      />
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </Card>

          {/* Improvements Card */}
          <Card className="p-8 shadow-card animate-fade-in-up" style={{ animationDelay: "0.2s" }}>
            <h3 className="font-display text-xl font-semibold text-foreground mb-6">
              Improvement Suggestions
            </h3>
            <div className="space-y-4">
              {improvements.map((item, index) => (
                <div 
                  key={index} 
                  className={`flex items-start gap-3 p-4 rounded-lg ${
                    item.positive ? "bg-success/5" : "bg-warning/5"
                  }`}
                >
                  <item.icon className={`h-5 w-5 mt-0.5 ${
                    item.positive ? "text-success" : "text-warning"
                  }`} />
                  <span className="text-foreground">{item.text}</span>
                </div>
              ))}
            </div>

            <div className="mt-8 p-4 rounded-lg bg-primary/5 border border-primary/10">
              <h4 className="font-medium text-foreground mb-2">Pro Tip</h4>
              <p className="text-sm text-muted-foreground">
                Tailor your resume for each job application. Use keywords from the job description 
                to improve your ATS compatibility score.
              </p>
            </div>
          </Card>
        </div>
      </div>
    </section>
  );
};

export default ScoreDisplay;
