import { FileText, Briefcase, TrendingUp } from "lucide-react";
import { Button } from "@/components/ui/button";

interface HeaderProps {
  activeTab: string;
  onTabChange: (tab: string) => void;
}

const Header = ({ activeTab, onTabChange }: HeaderProps) => {
  const navItems = [
    { id: "upload", label: "Upload Resume", icon: FileText },
    { id: "review", label: "Review Score", icon: TrendingUp },
    { id: "jobs", label: "Job Suggestions", icon: Briefcase },
  ];

  return (
    <header className="sticky top-0 z-50 w-full border-b border-border bg-card/80 backdrop-blur-md">
      <div className="container flex h-16 items-center justify-between">
        <div className="flex items-center gap-2">
          <div className="flex h-9 w-9 items-center justify-center rounded-lg gradient-primary">
            <FileText className="h-5 w-5 text-primary-foreground" />
          </div>
          <span className="font-display text-xl font-bold text-foreground">
            Resume<span className="text-gradient">AI</span>
          </span>
        </div>

        <nav className="hidden md:flex items-center gap-1">
          {navItems.map((item) => (
            <Button
              key={item.id}
              variant={activeTab === item.id ? "secondary" : "ghost"}
              onClick={() => onTabChange(item.id)}
              className={`gap-2 ${activeTab === item.id ? "bg-secondary" : ""}`}
            >
              <item.icon className="h-4 w-4" />
              {item.label}
            </Button>
          ))}
        </nav>

        <Button variant="gradient" size="sm">
          Get Started
        </Button>
      </div>
    </header>
  );
};

export default Header;
