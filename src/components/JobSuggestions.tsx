import { useState } from "react";
import { Search, SlidersHorizontal, Sparkles } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card } from "@/components/ui/card";
import JobCard from "./JobCard";
import { toast } from "sonner";

interface JobSuggestionsProps {
  isVisible: boolean;
}

const JobSuggestions = ({ isVisible }: JobSuggestionsProps) => {
  const [searchQuery, setSearchQuery] = useState("");
  const [appliedJobs, setAppliedJobs] = useState<string[]>([]);

  const jobs = [
    {
      id: "1",
      title: "Senior Frontend Developer",
      company: "TechCorp Inc.",
      location: "San Francisco, CA",
      salary: "$140K - $180K",
      type: "Full-time",
      matchScore: 92,
      logo: "T",
      postedAgo: "2 days ago",
      skills: ["React", "TypeScript", "Next.js", "Tailwind CSS", "GraphQL"],
    },
    {
      id: "2",
      title: "Full Stack Engineer",
      company: "StartupXYZ",
      location: "Remote",
      salary: "$120K - $160K",
      type: "Full-time",
      matchScore: 88,
      logo: "S",
      postedAgo: "1 week ago",
      skills: ["Node.js", "React", "PostgreSQL", "AWS", "Docker"],
    },
    {
      id: "3",
      title: "Software Engineer II",
      company: "BigTech Solutions",
      location: "New York, NY",
      salary: "$130K - $170K",
      type: "Full-time",
      matchScore: 85,
      logo: "B",
      postedAgo: "3 days ago",
      skills: ["Java", "Spring Boot", "React", "Kubernetes", "CI/CD"],
    },
    {
      id: "4",
      title: "UI/UX Developer",
      company: "DesignHub",
      location: "Austin, TX",
      salary: "$100K - $130K",
      type: "Full-time",
      matchScore: 78,
      logo: "D",
      postedAgo: "5 days ago",
      skills: ["Figma", "React", "CSS", "Animation", "Prototyping"],
    },
    {
      id: "5",
      title: "React Developer",
      company: "WebAgency Pro",
      location: "Chicago, IL",
      salary: "$90K - $120K",
      type: "Contract",
      matchScore: 75,
      logo: "W",
      postedAgo: "1 day ago",
      skills: ["React", "JavaScript", "Redux", "REST APIs", "Testing"],
    },
  ];

  const handleApply = (jobId: string) => {
    if (appliedJobs.includes(jobId)) {
      toast.info("You've already applied to this job");
      return;
    }
    setAppliedJobs([...appliedJobs, jobId]);
    toast.success("Application submitted successfully!", {
      description: "The employer will review your resume and get back to you.",
    });
  };

  const filteredJobs = jobs.filter(
    (job) =>
      job.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
      job.company.toLowerCase().includes(searchQuery.toLowerCase()) ||
      job.skills.some((skill) =>
        skill.toLowerCase().includes(searchQuery.toLowerCase())
      )
  );

  if (!isVisible) return null;

  return (
    <section className="py-16 md:py-24">
      <div className="container max-w-5xl">
        <div className="text-center mb-12 animate-fade-in-up">
          <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-primary/10 text-primary mb-4">
            <Sparkles className="h-4 w-4" />
            <span className="text-sm font-medium">AI-Powered Recommendations</span>
          </div>
          <h2 className="font-display text-3xl md:text-4xl font-bold text-foreground mb-4">
            Jobs <span className="text-gradient">Matched For You</span>
          </h2>
          <p className="text-lg text-muted-foreground max-w-2xl mx-auto">
            Based on your resume, we've found these opportunities that match your skills and experience
          </p>
        </div>

        {/* Search and Filter */}
        <Card className="p-4 mb-8 shadow-card animate-fade-in-up">
          <div className="flex flex-col sm:flex-row gap-4">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
              <Input
                placeholder="Search by title, company, or skill..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-10"
              />
            </div>
            <Button variant="outline" className="gap-2">
              <SlidersHorizontal className="h-4 w-4" />
              Filters
            </Button>
          </div>
        </Card>

        {/* Job Results Summary */}
        <div className="flex items-center justify-between mb-6 animate-fade-in-up">
          <p className="text-muted-foreground">
            Showing <span className="font-medium text-foreground">{filteredJobs.length}</span> matching jobs
          </p>
          <Button variant="ghost" size="sm" className="text-primary">
            Sort by: Best Match
          </Button>
        </div>

        {/* Job Cards */}
        <div className="space-y-4">
          {filteredJobs.map((job, index) => (
            <div
              key={job.id}
              className="animate-fade-in-up"
              style={{ animationDelay: `${index * 0.1}s` }}
            >
              <JobCard job={job} onApply={handleApply} />
            </div>
          ))}
        </div>

        {filteredJobs.length === 0 && (
          <Card className="p-12 text-center shadow-card">
            <p className="text-muted-foreground">No jobs found matching your search.</p>
          </Card>
        )}
      </div>
    </section>
  );
};

export default JobSuggestions;
